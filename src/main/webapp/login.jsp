<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<Title>App Direct Integration - Login</Title>

<script type="text/javascript">
function validateForm() {
    var x = document.forms[0]["user"].value;
    if (x == null || x == "") {
        alert("User Name must be filled out");
        return false;
    }
}

</script>
</head>
<body>

<h2>App Direct Integration</h2>

<!--Open IDs login-->&nbsp;
<form action="/LoginServlet?identifier=https://me.yahoo.com" method="post" onsubmit="return validateForm()"  >
User Name: <input name="user" />
<input type="submit" value="Login" /> </form>

</body>
</html>
