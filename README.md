# Corgi Gallery
---
Corgi Gallery is fast and simple remote gallery for Android based on async work, providing lightweight application.

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

## License

The MIT License (MIT)

Copyright (c) 2017 Dmitry Pashko

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
