<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="styles/style.css">
</head>
<body>
<div class="index-center">
    <div class="index-greetings">
		<span>
			<span id="login">LOGIN</span>
		</span>
    </div>

    <br>
    <br>
    <form id="index-loginform" action="/login" method="POST">
        <table id="loginformtable">
            <tr>
                <td>Username</td>
                <td><input type="text" class="textbox" name="username" id="loginname"></td>
            </tr>
            <tr>
                <td>Password</td>
                <td><input type="password" class="textbox" name="password" id="loginpasswd"></td>
            </tr>
            <tr>
                <td class="bottomrow"><a href="signup.jsp" id="index-signup">Don't have an account?</a></td>
                <td class="bottomrow"><input type="submit" class="index-button" id="login-button" value="GO!"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
