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
				<table>
					<tr><form action="/anagrams" method="get"> 
						<!-- this is to check for anagrams -->
						<td>Check for anagrams: </td><td><input type="text" name="check_anagram"/></td>
						<td><input type="submit" value="Check"/></td>
					</form></tr>
					<tr><form action="/anagrams" method="post">
						<!-- this is to add new words to the engine -->
						<td>Add to dictionary: </td><td><input type="text" name="submit_word"/></td>
						<td><input type="submit" value="Submit"/><br/></td>
					</form></tr>
				</table>
				<c:if test="${errMsg != null}">
					<script type="text/javascript">
						   alert("${errMsg}");
					</script>
				</c:if>
				<c:if test ="${displayMsg != null}">
					<br/><h3>${displayMsg}</h3>
				</c:if>
			</c:when> 
			<c:otherwise> 
				<p> Welcome! <a href="${login_url}">Sign in or register</a> </p> 
			</c:otherwise> 
		</c:choose> 
	</body>
</html>