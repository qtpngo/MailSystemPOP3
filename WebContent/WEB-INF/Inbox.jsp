<%@page import="org.apache.jasper.tagplugins.jstl.core.ForEach"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%
	ArrayList<MessageListAdapter> listMessages = (ArrayList<MessageListAdapter>) request
			.getAttribute("listMessages");
	String message = (String) request.getAttribute("message");
	if (message == null) {
		message = "";
	}
	if (listMessages.isEmpty() && "".equals(message)){
		message = "Inbox is empty!";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Quang Ngo">

<title>Inbox</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Code Javascript -->
<script src="js/bootstrap.min.js"></script>
</head>
<body>
	<div class="container-fluid">
		<div class="panel panel-warning" style="margin: 10px auto;">
			<div class="panel-heading">
				<h4>Inbox</h4>
			</div>
			<div class="panel-body">
				<table class="table table-hover">
					<tr>
						<th>Subject</th>
						<th>From</th>
						<th>Time</th>
						<th></th>
					</tr>
					<%
						for (MessageListAdapter messageListAdapter : listMessages) {
							boolean isRead = messageListAdapter.getContent().contains("QMS is read");
							if (messageListAdapter.getContent().contains("QMS is deleted")) {
								continue;
							}
					%>
					<tr>
						<td><a
							href="OthersController?act=DetailInbox&time=<%=messageListAdapter.getTime()%>"
							style="font-weight: <%=isRead ? "" : "bold"%>;"><%=messageListAdapter.getSubject()%></a></td>
						<td><p style="font-weight: <%=isRead ? "" : "bold"%>;"><%=messageListAdapter.getFrom()%></p></td>
						<td><p style="font-weight: <%=isRead ? "" : "bold"%>;"><%=messageListAdapter.getTime()%></p></td>
						<td><a
							href="OthersController?act=Delete&subject=<%=messageListAdapter.getSubject()%>&from=<%=messageListAdapter.getFrom()%>&time=<%=messageListAdapter.getTime()%>"
							class="btn btn-danger"><span
								class="glyphicon glyphicon-trash"></span></a></td>
					</tr>
					<%
						}
					%>
				</table>
			</div>
			<div class="panel-footer">
				<b style="color: red;"><%=message%></b>
			</div>
		</div>
	</div>
</body>
</html>