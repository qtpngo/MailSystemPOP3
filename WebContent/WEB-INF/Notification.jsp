<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%	String message = (String) request.getAttribute("message"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="Quang Ngo">
 
        <title>Notification</title>
 
        <!-- Bootstrap Core CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">

        <!-- jQuery -->
        <script src="js/jquery.min.js"></script>

        <!-- Bootstrap Code Javascript -->
        <script src="js/bootstrap.min.js"></script>
    </head>
    <body>
    	<div class="container-fluid">
    		<b style="color: red; margin: 5px auto;"><%=message %></b>
    	</div>
    </body>
</html>