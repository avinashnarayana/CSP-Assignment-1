<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html> 
<html lang="en"> 
	<head> 
		<meta charset="UTF-8"> 
		<title>Assignment 1</title> 
	</head> 
	<body> 
		<!-- if the user is logged in then we need to render one 
		version of the page consequently if the user is logged out we need to render a different version
 		of the page
		--> 	
		<c:choose> 
			<c:when test="${user != null}">
				<p> 
					Welcome ${user.email} <br/>
					You can signout <a href="${logout_url}">here</a><br/>
				</p>
				<form action="/" method="post"> 
					<!-- this is how you get a single line of text from the user -->
					Enter a string here to check for anagrams: <input type="text" name="text_input"/>
					<input type="submit" value="Check"/><br/>
				</form>
				<c:if test="${errMsg != null}">
					<script type="text/javascript">
						   alert("${errMsg}");
					</script>
				</c:if>
				<c:if test ="${result != null}">
					<h1>Results</h1>
					<p>Anagrams of the input word previously stored on the database are: ${result}</p>
				</c:if>
			</c:when> 
			<c:otherwise> 
				<p> Welcome! <a href="${login_url}">Sign in or register</a> </p> 
			</c:otherwise> 
		</c:choose> 
	</body>
</html>