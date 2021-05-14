
# Adaptive Auth

<p align="center">
<img  src="https://github.com/Senthuran100/AdaptiveLogin/blob/main/login-app-client/src/Logo.png" />
</p>

## Overview
<p align="center">
<img  src="https://github.com/Senthuran100/AdaptiveLogin/blob/main/LitReview_Architecture_PIC.jpg" />
</p>

## Steps to Setup the Spring Boot Back end app (login-app-server)


1. **Change MySQL username and password as per your MySQL installation**
	install MySQL Community Server

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

2. **Run the app**

	You can run the spring boot app with spring tool suite:

	import the login-app-server

	run as > spring boot app

	The server will start on port 5000. The spring boot app includes the front end build also, so you'll be able to access the complete application on `http://localhost:5000`.

3. **Add the default Roles**

	Any new user who signs up to the app is assigned the `ROLE_USER` by default.

## Steps to Setup the React Front end app (login-app-client)

First go to the `login-app-client` folder -

```bash
cd login-app-client
```

Then type the following command to install the dependencies and start the application -

```bash
npm install
npm start
```

The front-end server will start on port `3000`.

## CSV Writer

 * CSV Writer can be used to write the data from Mysql DB to CSV. Open the CSVWriter and run the project.
    There will be a `LoginParam.csv` file will be created inside the `{PROJECT_FOLDER}\CSVWriter\target\classes` folder.
