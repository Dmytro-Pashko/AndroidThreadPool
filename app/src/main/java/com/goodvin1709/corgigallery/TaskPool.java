package com.goodvin1709.corgigallery;

public interface TaskPool {

    void addTaskToPool(Runnable task);

    void stopAll();
}
