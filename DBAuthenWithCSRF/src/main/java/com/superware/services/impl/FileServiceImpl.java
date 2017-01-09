package com.superware.services.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.superware.exception.ApplicationException;
import com.superware.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public Map<Object, Object> readExcel(MultipartFile file) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		try {
			System.out.println(file.getBytes().length);
			System.out.println(file.getContentType());
			System.out.println(file.getOriginalFilename());
			if (!file.getContentType().equals("application/vnd.ms-excel")){
				throw new ApplicationException("Content type not allowed");
			}
			HSSFWorkbook wb = new HSSFWorkbook(file.getInputStream());
			
			for(int i = 0; i<wb.getNumberOfSheets(); i++){
				HSSFSheet sheet = wb.getSheetAt(i);
				System.out.println("Sheet " + i + " \"" + wb.getSheetName(i) + "\" has " + sheet.getPhysicalNumberOfRows() + " row(s).");
				for (Row row : sheet){
					for (Cell cell : row){
						CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
						System.out.println(cellRef.formatAsString());
						System.out.println(" - ");
						
						//get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
						String text = new DataFormatter().formatCellValue(cell);
						System.out.println(text);
						//Alternatively, get the value and format it yourself
						switch(cell.getCellTypeEnum()){
							case STRING:
								System.out.println(cell.getRichStringCellValue().getString());
								break;
							case NUMERIC:
								if(DateUtil.isCellDateFormatted(cell)){
									System.out.println(cell.getDateCellValue());
								} else {
									System.out.println(cell.getNumericCellValue());
								}
								break;
							case BOOLEAN:
								System.out.println(cell.getBooleanCellValue());
								break;
							case FORMULA:
								System.out.println(cell.getCellFormula());
								break;
							case BLANK:
								System.out.println("Blank");
								break;
							default:
								System.out.println("Default");
						}
						
						result.put(wb.getSheetName(i) + " " + cellRef.formatAsString(), text);
					}
				}
			}
			
			wb.close();
		} catch (IOException e) {
			throw new ApplicationException(e.getMessage());
		}	
		return result;
	}
}
