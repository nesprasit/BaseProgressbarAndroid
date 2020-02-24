![MinSdk](https://img.shields.io/badge/minSdk-19-green.svg)
[![jCenter](https://img.shields.io/badge/jCenter-1.0.0-green.svg)](https://bintray.com/okanesboy/library/com.nesprasit.design/_latestVersion)
[![](https://img.shields.io/badge/License-Apache_v2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
# Base Progressbar Android
Base Progressbar is component for Android

## Installation
Maven
```
<dependency>
  <groupId>com.nesprasit</groupId>
  <artifactId>baseprogressbar</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
Gradle
```
implementation 'com.nesprasit:baseprogressbar:1.0.0'
```

## How to use
```
<com.nesprasit.library.BaseProgressbar
    android:id="@+id/progress"
    android:layout_width="0dp"
    android:layout_height="20dp"
    app:progress="50"
    app:max="100"
    app:padding="2dp"
    app:radius="5dp"
    app:progressBackgroundColor="#E0E0E
    app:progressColor="#00C853"
    app:progressStrokeColor="#00C853"
    app:progressStrokeWidth="1dp" />
```
![](./images/progressbar.gif)

## License
```
Copyright (C) 2020 Nesprasit

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
