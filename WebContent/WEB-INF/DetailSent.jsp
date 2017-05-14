<%@page import="java.util.ArrayList"%>
<%@ page import="utilities.*"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>
<%
	MessageListAdapter messageListAdapter = (MessageListAdapter) request.getAttribute("messageListAdapter");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Quang Ngo">

<title>Detail</title>

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
		<div class="panel panel-success" style="margin: 10px auto;">
			<div class="panel-heading">
				<h4>Detail</h4>
			</div>
			<div class="panel-body">
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-2">
						<label>To</label>
					</div>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="receipient"
							value="<%=messageListAdapter.getFrom()%>" readonly>
					</div>
				</div>
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-2">
						<label>Time</label>
					</div>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="receipient"
							value="<%=messageListAdapter.getTime()%>" readonly>
					</div>
				</div>
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-2">
						<label>Subject</label>
					</div>
					<div class="col-sm-10">
						<input class="form-control" type="text" name="subject"
							value="<%=messageListAdapter.getSubject()%>" readonly>
					</div>
				</div>
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-2">
						<label>Content</label>
					</div>
				</div>
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-12">
						<%
							String _content = messageListAdapter.getContent();
							_content = _content.replace("QMS is read", "");
						%>
						<textarea class="form-control" rows="12" name="content" readonly><%=_content%></textarea>
					</div>
				</div>
				<%
					if (!"".equals(messageListAdapter.getAttachment())) {
				%>
				<div class="row" style="margin: 10px auto;">
					<div class="col-sm-12">
						<a
							href="DownloadFileAttachmentController?attachment=<%=messageListAdapter.getAttachment()%>">Attachment
							File</a>
					</div>
				</div>
				<%
					}
				%>
			</div>
			<div class="panel-footer">
				<div class="row">
					<div class="col-sm-offset-8 col-sm-2">
						<button class="btn btn-default btn-block" type="button"
							onFocus="goBack();">Back</button>
					</div>
					<div class="col-sm-2">
						<a
							href="OthersController?act=Reply&receipient=<%=messageListAdapter.getFrom()%>&subject=RE:<%=messageListAdapter.getSubject()%>"
							class="btn btn-info btn-block">Reply</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>