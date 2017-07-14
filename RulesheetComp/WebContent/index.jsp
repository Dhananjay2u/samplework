<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Rulesheet Comparison</title>

<script type="text/javascript">
function validate(form)
{
		if(!(form.existingfile.value!="" && form.newfile.value!=""))
			{
				alert("Please select the files to be compare...")
				return false;
			}
}

</script>
</head>
<body>
<center>
<h2>Rulesheet Comparison</h2>
</center>

<center>
<form method="post" action="CompareRuleController" enctype="multipart/form-data" onsubmit="return validate(this)">
<table>
<tbody style="background-color: aqua;font: verdana;">
<tr><td>Existing Rulesheet</td><td><input type="file" name="existingfile"></td></tr>
<tr><td>New Rulesheet</td><td><input type="file" name="newfile"></td></tr>
<tr><td><input type="reset"></td><td><input type="submit" name="comparerule" value="CompareRulesheet"></td></tr>
</tbody>
</table>
</form>

<h4 style="color: blue;font-weight: bold;">Note : Filename in both places should have same name and type...</h4>

<br/>
<% if( request.getParameter("compFilepath")!=null){ %>
<h3>Output File after Comparison</h3>
<a href="DownloadController?filepath=<%= request.getParameter("compFilepath") %>">Click to download</a> Comparison Result. <br/> Filename : <%= request.getParameter("compFilepath") %>
<%} %>
<% if( request.getParameter("err")!=null){ %>
<h3 style="color: red;"><%= request.getParameter("err")%></h3>
<%} %>
</center>
</body>
</html>