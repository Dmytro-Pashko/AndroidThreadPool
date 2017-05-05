#Corgi Gallery
---
Corgi Gallery is fast, simple remote gallery for Android based on async work, without dependencies for other 
library providing lightweight APK file less 100 kb.

Application based on two caches :

1. Caching in memory using LRUCache;
2. Caching in external storage;

For manage tasks and threads used **ThreadPoolExecutor**.

Application based on four tasks:
* Download images list task;
* Download image task;
* Save image to external cache;
* Load image into memory cache;

Corgi gallery it's example to communication between worker thread and UI thread and restore state changing configuration.
Application download resources from github repository folder [link](https://github.com/goodvin1709/AndroidThreadPool/tree/master/images)

Images come in all shapes and sizes. In many cases they are larger than required for a typical 
application user interface (UI).
This application demonstrated how to best work with bitmaps without OutOfMemoryException and etc, when 
bitmap resolution higher then ImageView dimens and screen density of your device.

**Image flow**:
1. Download image
2. Save image into External Cache
3. Save image into Memory
4. Load into ImageView;

