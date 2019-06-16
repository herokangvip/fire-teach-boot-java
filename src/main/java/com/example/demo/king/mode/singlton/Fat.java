package com.example.demo.king.mode.singlton;

public class Fat {
    private String id;
    private String name;

    public Fat() {
    }

    public Fat(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Fat fat = (Fat) object;

        if (id != null ? !id.equals(fat.id) : fat.id != null) return false;
        return name != null ? name.equals(fat.name) : fat.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
