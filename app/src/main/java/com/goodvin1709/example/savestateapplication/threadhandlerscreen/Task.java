package com.goodvin1709.example.savestateapplication.threadhandlerscreen;

class Task implements Runnable {

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
