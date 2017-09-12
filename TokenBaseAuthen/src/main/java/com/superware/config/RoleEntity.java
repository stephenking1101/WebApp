package com.superware.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrii on 14.03.2016.
 */
public class RoleEntity{

    private Integer id;
    private RolesEnum name;
    private List<PermissionEntity> permissions = new ArrayList<PermissionEntity>();

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RolesEnum getName() {
        return name;
    }

    public void setName(RolesEnum name) {
        this.name = name;
    }

    public List<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionEntity> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleEntity that = (RoleEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (permissions != null ? !permissions.equals(that.permissions) : that.permissions != null) return false;

        return true;
    }

    public boolean equals(String o) {
        if (name != null ? !name.equals(o) : o != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (permissions != null ? permissions.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
