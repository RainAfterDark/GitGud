package org.gitgud.core.model;

import java.util.List;

public abstract class Post {
    int x, y;
    int width, height;
    List<String> lines;

    void calcDimensionsFromLines() {
        if (lines == null) return;
        width = lines.stream().mapToInt(String::length).max().orElse(0) + 4;
        height = lines.size() + 2;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    abstract public void update(double deltaTIme);
}
