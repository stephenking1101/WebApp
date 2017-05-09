/**
 * Copyright (c) 2003 held jointly by the individual authors.            
 *                                                                          
 * This library is free software; you can redistribute it and/or modify it    
 * under the terms of the GNU Lesser General Public License as published      
 * by the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.                                            
 *                                                                            
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; with out even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.                                                  
 *                                                                           
 * You should have received a copy of the GNU Lesser General Public License   
 * along with this library;  if not, write to the Free Software Foundation,   
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.              
 *                                                                            
 * > http://www.gnu.org/copyleft/lesser.html                                  
 * > http://www.opensource.org/licenses/lgpl-license.php
 */
package net.mlw.vlh.adapter.hibernate3;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.mlw.vlh.DefaultListBackedValueList;
import net.mlw.vlh.ValueList;
import net.mlw.vlh.ValueListInfo;
import net.mlw.vlh.adapter.AbstractValueListAdapter;
import net.mlw.vlh.adapter.hibernate3.util.ScrollableResultsDecorator;
import net.mlw.vlh.adapter.util.ObjectValidator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/** 
 * Subclass need to implement criteria for given info parameters. <code>getCriteria(info, session)</code>
 * Initial spike implementation, some fields are not truely required. Need to be rework.
 * ContentProvider is better name for Adapter
 * @author <A HREF="mailto:azachar@users.sourceforge.net">Andrej Zachar</A>
 * @version $Revision:$ $Date:$
 */
abstract public class AbstractCriteriaContentProvider extends AbstractValueListAdapter {

    /** Commons logger. */
    private static final Log logger = LogFactory.getLog(AbstractCriteriaContentProvider.class);

    /** The Hibernate SessionFactory. */
    private SessionFactory sessionFactory;

    /**
     * <p>
     * If is set, it use special ScrollableResultsDecorator, that enable or disable to add object in final list.
     * </p>
     * <h4>NOTE:</h4>
     * <p>
     * Also, it respects the total count of entries that overlap your paged list.
     * </p>
     */
    private ObjectValidator _validator = null;

    /** If a new Session should be created if no thread-bound found. */
    private boolean allowCreate = true;

    /**
     * The max rows in ResulSet to doFocus
     * 
     * @author Andrej Zachar
     */
    private long maxRowsForFocus = Long.MAX_VALUE;

    /**
     * <p>
     * If is set, it use special ScrollableResultsDecorator, that enable or disable to add object in final list.
     * </p>
     * <h4>NOTE:</h4>
     * <p>
     * Also, it respects the total count of entries that overlap your paged list.
     * </p>
     * 
     * @param validator The validator to set.
     */
    public void setValidator(ObjectValidator validator) {
        _validator = validator;
    }

    /**
     * @see net.mlw.vlh.ValueListAdapter#getValueList(java.lang.String, net.mlw.vlh.ValueListInfo)
     */    
    public ValueList getValueList(String name, ValueListInfo info) {

        logger.debug("getValueList(String, ValueListInfo) - start");

        if (info.getSortingColumn() == null) {
            info.setPrimarySortColumn(getDefaultSortColumn());
            info.setPrimarySortDirection(getDefaultSortDirectionInteger());
            if (logger.isDebugEnabled()) {
                logger.debug("The default sort column '" + getDefaultSortColumn() + "' with direction '" + getDefaultSortDirectionInteger()
                        + "' was  set.");
            }
        }

        int numberPerPage = info.getPagingNumberPer();

        if (numberPerPage == Integer.MAX_VALUE) {
            numberPerPage = getDefaultNumberPerPage();
            info.setPagingNumberPer(numberPerPage);
            if (logger.isDebugEnabled()) {
                logger.debug("The paging number per page '" + numberPerPage + "' was  set.");
            }
        }

        Session session = SessionFactoryUtils.getSession(getSessionFactory(), allowCreate);
        try {
            Criteria query;

            boolean doFocus = ((getAdapterType() & DO_FOCUS) == 0) && info.isFocusEnabled() && info.isDoFocus();

            if (doFocus) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Start to focusing adapterName '" + name + "', ValueListInfo info = " + info + "'");
                }
                ScrollableResults results = getScrollableResults(getQueryForFocus(info, session), info);
                results.beforeFirst();
                doFocusFor(info, results);

                if (logger.isDebugEnabled()) {
                    logger.debug("Focusing finished for adapterName '" + name + "', ValueListInfo info '" + info + "'");
                }
            }

            query = getQuery(info, session);

            boolean doPaging = ((getAdapterType() & DO_PAGE) == 0);

            List list;

            if (doPaging) {
                if (logger.isDebugEnabled()) {
                    logger.debug("getValueList(String adapterName = " + name + ", ValueListInfo info = " + info + ") - Start to paging result set");
                }

                list = new ArrayList(numberPerPage);
                ScrollableResults results = getScrollableResults(query, info);

                results.last();
                int lastRowNumber = results.getRowNumber();
                info.setTotalNumberOfEntries(lastRowNumber + 1);

                if (numberPerPage == 0) {
                    numberPerPage = getDefaultNumberPerPage();
                }

                int pageNumber = info.getPagingPage();
                boolean isResult;
                if (pageNumber > 1) {
                    if ((pageNumber - 1) * numberPerPage > lastRowNumber) {
                        pageNumber = (lastRowNumber / numberPerPage) + 1;
                        info.setPagingPage(pageNumber);
                    }
                }
                if (pageNumber > 1) {
                    isResult = results.scroll((pageNumber - 1) * numberPerPage - lastRowNumber);
                } else {
                    isResult = results.first();
                }

                for (int i = 0; i < numberPerPage && isResult; i++) {
                    list.add(results.get(0));
                    isResult = results.next();
                }

                logger.debug("Sorting finished.");

            } else {

                logger.debug("Retrieving a list directly from the query.");

                list = query.list();
                info.setTotalNumberOfEntries(list.size());
            }

            ValueList returnValueList = createValueList(info, list);
            if (logger.isDebugEnabled()) {
                logger.debug("Retrieved list was wrapped in valuelist, info=" + info);
            }
            return returnValueList;
        } catch (HibernateException e) {
            logger.error("Error getting data in adapater '" + name + "' with info = '" + info + "'", e);
            throw SessionFactoryUtils.convertHibernateAccessException(e);
        } catch (Exception e) {
            logger.fatal("Fatal error getting data in adapater '" + name + "' with info = '" + info + "'", e);
            return null;
        } finally {
            SessionFactoryUtils.releaseSession(session, getSessionFactory());
        }
    }

    /**
     * @param info
     * @param list
     * @return DefaultListBackValueList instance
     */
    protected ValueList createValueList(ValueListInfo info, List list) {
        return new DefaultListBackedValueList(list, info);
    }

    /**
     * @param info
     * @param results
     * @throws HibernateException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void doFocusFor(ValueListInfo info, ScrollableResults results) throws HibernateException {
        info.setFocusStatus(ValueListInfo.FOCUS_NOT_FOUND);

        int currentRow;

        if (logger.isDebugEnabled()) {
            logger.debug("Focusing only property '" + info.getFocusProperty() + "' == '" + info.getFocusValue() + "'.");
        }
        for (currentRow = 0; ((results.next()) && (currentRow < maxRowsForFocus)); currentRow++) {
            Object value = results.get(0);
            if (value.equals(info.getFocusValue())) {
                if (logger.isInfoEnabled()) {
                    logger.info("Focus property '" + info.getFocusProperty() + "' in row '" + currentRow + "'.");
                }
                info.setPagingPageFromRowNumber(results.getRowNumber());
                info.setFocusedRowNumberInTable(results.getRowNumber());
                info.setFocusStatus(ValueListInfo.FOCUS_FOUND);
                break;
            }
        }

        if (currentRow == maxRowsForFocus) {
            if (logger.isInfoEnabled()) {
                logger.info("Focus for property '" + info.getFocusProperty() + "' exceded maximum rows for focus '" + maxRowsForFocus + "'.");
            }
            info.setFocusStatus(ValueListInfo.FOCUS_TOO_MANY_ITEMS);
        }
    }

    /**
     * @param query
     * @param info ValueListInfo This info will be set to validator.
     * @return ScrollableResults, if is set non null _validator, it returns the ScrollableResultsDecorator.
     * @throws HibernateException
     */
    private ScrollableResults getScrollableResults(Criteria criteria, ValueListInfo info) throws HibernateException {
        ScrollableResults results;

        if (_validator == null) {
            logger.debug("Validator is null, using normal ScrollableResults");
            results = criteria.scroll();

        } else {
            logger.info("Using decorator of the ScrollableResults with your validator.");
            _validator.setValueListInfo(info);
            results = new ScrollableResultsDecorator(criteria.scroll(), _validator);
        }

        return results;
    }

    /**
     * Subclass can add unique result
     * 
     * @param info
     * @param session
     * @return
     * @throws HibernateException
     */
    protected Criteria getQuery(ValueListInfo info, Session session) throws HibernateException {
        if (logger.isDebugEnabled()) {
            logger.debug("Create criteria");
        }
        Criteria criteria = getCriteria(info, session);

        if (logger.isDebugEnabled()) {
            logger.debug("Apply sorting");
        }
        applySorting(info, criteria);

        if (logger.isInfoEnabled()) {
            logger.info("Final criteria:" + criteria);
        }
        return criteria;
    }

    /**
     * Apply sorting based on info
     * 
     * @param info
     * @param criteria
     */
    protected void applySorting(ValueListInfo info, Criteria criteria) {
        applySortingOnColumn(info, criteria, info.getSortingColumn());
    }

    /**
     * @param info
     * @param criteria
     * @param sortingColumn
     */
    protected void applySortingOnColumn(ValueListInfo info, Criteria criteria, final String sortingColumn) {
        Order order;
        if (info.getSortingDirection() == ValueListInfo.ASCENDING) {
            order = Order.asc(sortingColumn);
        } else {
            order = Order.desc(sortingColumn);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("added sorting " + order);
        }
        criteria.addOrder(order);
    }

    /**
     * Subclass need to implement defined criteria
     * 
     * @param info
     * @param session
     * @return
     */
    abstract protected Criteria getCriteria(ValueListInfo info, Session session);

    /**
     * If focus optimalization is true, it select only focus property. For validator is recommended to set it to false, while you want to validate
     * properties of retrieved objects.
     * 
     * @param info
     * @param session
     * @return
     * @throws HibernateException
     */
    protected Criteria getQueryForFocus(ValueListInfo info, Session session) throws HibernateException {
        if (logger.isDebugEnabled()) {
            logger.debug("getting query for focus");
        }
        Criteria result = getQuery(info, session);
        applyFocusProjection(info, result);
        if (logger.isDebugEnabled()) {
            logger.debug("final query for focus is: " + result);
        }
        return result;

    }

    /**
     * @param info
     * @param result
     */
    protected void applyFocusProjection(ValueListInfo info, Criteria result) {
        Projection focusProjection = createFocusProjection(info);
        if (focusProjection != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("applying focus projection " + focusProjection);
            }
            result.setProjection(focusProjection);
        }
    }

    /**
     * @param info
     * @return
     */
    protected Projection createFocusProjection(ValueListInfo info) {
        return Projections.property(info.getFocusProperty());
    }

    /**
     * Set the Hibernate SessionFactory to be used by this DAO.
     * 
     * @param sessionFactory The Hibernate SessionFactory to be used by this DAO.
     */
    public final void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Return the Hibernate SessionFactory used by this DAO.
     * 
     * @return The Hibernate SessionFactory used by this DAO.
     */
    protected final SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    /**
     * Sets: If a new Session should be created if no thread-bound found.
     * 
     * @param allowCreate The allowCreate to set.
     */
    public void setAllowCreate(boolean allowCreate) {
        this.allowCreate = allowCreate;
    }

    /**
     * Maximum rows to search with Focus
     * 
     * @return Returns the maxRowsForFocus.
     */
    public long getMaxRowsForFocus() {
        return maxRowsForFocus;
    }

    /**
     * Maximum rows to search with Focus
     * 
     * @param maxRowsForFocus The maxRowsForFocus to set.
     */
    public void setMaxRowsForFocus(long maxRowsForFocus) {
        this.maxRowsForFocus = maxRowsForFocus;
    }

}
