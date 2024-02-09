# Pedometer
This app is built using Jetpack Compose which tracks user's daily steps using `TYPE_STEP_COUNTER` sensor.
The app locally stores the steps history, also includes a bar chart to compare the progress against the set target. 
It uses [scrollablebarchart-compose](https://github.com/Varsha-Kulkarni/scrollablebarchart-compose) to plot the barchart.

This app is built with following Android Dev Features:

- Jetpack Compose for simple UI
- Foreground Service for Steps Updates
- Room Database for persisting every day steps
- Hilt for DI
- Kotlin State Flow for collecting emitted step counts
- Kotlin Coroutines for interacting with DB
- UDF with modern android dev Jetpack components and Google recommended architecture.

##  Screenshots

<img src="/results/screenshot_1.jpeg" width="160">&emsp;<img src="/results/screenshot_2.jpeg" width="160">
<img src="/results/screenshot_3.jpeg" width="160">&emsp;<img src="/results/screenshot_4.jpeg" width="160">


# Contributions

Most welcome to file issues, PRs.

# License

```
Copyright 2023 Varsha Kulkarni
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    https://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
