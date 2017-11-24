<%--
  Created by IntelliJ IDEA.
  User: agoun
  Date: 11/2/2017
  Time: 10:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="/register" method="post">
    <table id="signupformtable">
        <tr>
            <td class="property">Your Name</td>
            <td class="text"><input type="text" class="index-textbox" id="name" name="name"></td>
        </tr>

        <tr>
            <td class="property">Username</td>
            <td class="text"><input type="text" class="index-textbox" id="username" name="username">
                <span id="dupename">&#x2713</span></td>
        </tr>

        <tr>
            <td class="property">Email</td>
            <td class="text"><input type="text" class="index-textbox" id="email" name="email">
                <span id="dupeemail">&#x2713</span></td>
        </tr>

        <tr>
            <td class="property">Password</td>
            <td class="text"><input type="password" class="index-textbox" id="passwd" name="password"></td>
        </tr>

        <tr>
            <td class="property">Confirm Password</td>
            <td class="text"><input type="password" class="index-textbox" id="confirmpasswd" name="confirmpassword"></td>
        </tr>

        <tr>
            <td class="property">Phone Number</td>
            <td class="text"><input type="text" class="index-textbox" id="phoneNumber" name="phonenumber"></td>
        </tr>

        <tr>
            <td colspan="2"><input type="checkbox" class="index-checkbox" name="asdriver">Also sign me up as a driver!</td>
        </tr>

        <tr>
            <td class="bottomrow"><a href="../index.php" id="index-signup">Already have an account?</a></td>
            <td class="bottomrow"><input type="submit" class="index-button" id="signup-button" value="REGISTER""></td>
        </tr>
    </table>
</form>

</body>
</html>
