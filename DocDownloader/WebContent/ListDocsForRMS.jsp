<%@page import="java.util.ArrayList"%>
<%@page import="java.io.File"%>
<%@page import="org.sc.util.FileFinder"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of Docs Attached for Given RMS No...</title>
</head>
<body>

<h1>List of Docs Attached for Given RMS No...</h1>

<% 

String filePath=request.getParameter("filepath");
		
		if(filePath==null)
		{
%>
<h3>No Path is given to fetch files.</h3>
<%
			return;
		}
		filePath=filePath.substring(0,filePath.indexOf(":")-3)+""+filePath.substring(filePath.indexOf(":")+8);
		System.out.println(filePath);

		if(!FileFinder.isExist(filePath))
		{
			%>
			<h3>Given Path does not exist to fetch files.</h3>
			<%
						return;
					}
		
		ArrayList<File> files =FileFinder.getFiles(filePath);
		for(File file:files){
			if (file.isFile())
			{
			String mime=FileFinder.getMimeType(file.getName().substring(file.getName().lastIndexOf(".")+1));
	
%>
<h3><a href="DocDownloader?filepath=<%=filePath%><%=file.getName()%>&contentType=<%=mime%>"><%=file.getName()%></a></h3>
<%
			}
}
%>

</body>
</html>