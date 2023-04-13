

If you havent used java at all follow these steps<br>

1. Install Java Development Kit (JDK):
You need to have JDK installed on your system to run the Java Spring application. If you do not have it already, you can download it from the official Oracle website. Follow the installation instructions to complete the installation.(https://www.oracle.com/java/technologies/javase/jdk19-archive-downloads.html) 
you need exactly java 19 jdk in order to run it properly

2. Set the JAVA_HOME environment variable:
After installing JDK, you need to set the JAVA_HOME environment variable. This variable points to the directory where JDK is installed. The exact process of setting this variable depends on the operating system you are using. After installation, set up the environment variables for Java. On Windows, this can be done by going to Control Panel > System > Advanced system settings > Environment Variables, and then adding the path to the Java installation directory to the "Path" variable.

![thirdImage](https://user-images.githubusercontent.com/35383291/231607001-eaa32277-7ca4-4c6f-9437-ba0432bf3b34.png)<br>
![FourthImage](https://user-images.githubusercontent.com/35383291/231607022-f9a982bc-a3fc-421a-acf2-11a5d8e4cda4.png)<br>
![5Image](https://user-images.githubusercontent.com/35383291/231607029-1360135e-ed51-4138-827b-9c209e9916e3.png)<br>

Add here the path to you JDK installation, it usually C:\Program Files\Java\jdk-19
![Image6](https://user-images.githubusercontent.com/35383291/231607034-2ea287c1-d4ba-447c-9836-217eb3401d08.png)
Click ok and then apply , that should save the jdk to path.

Follow the next images so the java compiler can work properly
![7Image](https://user-images.githubusercontent.com/35383291/231608115-76ea74db-0ee0-414b-b582-b5e295610b40.png)<br>
![8Image](https://user-images.githubusercontent.com/35383291/231608121-e390d9b8-2386-4d95-b4a4-c6e8ed84f753.png)<br>

here add where u saved the jdk file isually its C:\Program Files\Java\jdk-19
![9Image](https://user-images.githubusercontent.com/35383291/231608137-06df05b3-1d89-4aab-9532-a517eae982ae.png)<br>



3.Re-Clone the project i added a new dependecny so you dont have to install additional packages
Inside of the app Go to Demo-> src -> main -> resources -> application.properties

![FirstImage](https://user-images.githubusercontent.com/35383291/231598443-67989536-934c-4959-9755-6a3e259616aa.png)

1 - put the name of your database there  <br>
2 - put the username that you use (default username in postgres is "postgres")  <br>
3 - put password to database  (default is empty, leave it blank)
![SecondImage](https://user-images.githubusercontent.com/35383291/231598864-bb3219bb-1dfe-43a5-a5e5-cd8d3c44f92b.png)

4. Run the application and then run ->   http://localhost:8080/api/test in your browser<br>
That should populate your database


