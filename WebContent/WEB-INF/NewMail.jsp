<%@page import="java.util.ArrayList"%>
<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%
	String to = (String) request.getAttribute("receipient");
	String subject = (String) request.getAttribute("subject");
	if (to == null || subject == null) {
		to = "";
		subject = "";
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

<title>New mail</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- jQuery -->
<script src="js/jquery.min.js"></script>

<!-- Bootstrap Code Javascript -->
<script src="js/bootstrap.min.js"></script>
<script type="text/javascript">
	function goBack() {
		window.history.back();
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<form action="SendMailController?act=NewMail" method="post"
			enctype="multipart/form-data">
			<div class="panel panel-info" style="margin: 10px auto;">
				<div class="panel-heading">
					<h4>New mail</h4>
				</div>
				<div class="panel-body">
					<div class="row" style="margin: 10px auto;">
						<div class="col-sm-2">
							<label>To</label>
						</div>
						<div class="col-sm-10">
							<input class="form-control" type="text" name="receipient"
								value="<%=to%>" placeholder="Ex: abc@ngoquang.com">
						</div>
					</div>
					<div class="row" style="margin: 10px auto;">
						<div class="col-sm-2">
							<label>Subject</label>
						</div>
						<div class="col-sm-10">
							<input class="form-control" type="text" name="subject"
								value="<%=subject%>" placeholder="Your email subject">
						</div>
					</div>
					<div class="row" style="margin: 10px auto;">
						<div class="col-sm-2">
							<label>Attachment</label>
						</div>
						<div class="col-sm-10">
							<input class="form-control" type="file" name="attachment">
						</div>
					</div>
					<div class="row" style="margin: 10px auto;">
						<div class="col-sm-2">
							<label>Content</label>
						</div>
					</div>
					<div class="row" style="margin: 10px auto;">
						<div class="col-sm-12">
							<textarea class="form-control" rows="12" name="content"></textarea>
						</div>
					</div>
				</div>
				<div class="panel-footer">
					<div class="row">
						<div class="col-sm-offset-8 col-sm-2">
							<button class="btn btn-default btn-block" type="button"
								onFocus="goBack();">Cancel</button>
						</div>
						<div class="col-sm-2">
							<button class="btn btn-info btn-block" type="submit">Send</button>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>