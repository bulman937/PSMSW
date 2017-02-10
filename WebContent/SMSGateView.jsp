<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>${balance}</h1>
${buffer}
<h1>Send single SMS</h1>
<form action="gate" method="POST" name="singleForm">
	<input name="type" type="hidden" value="single">
	Target number
	<input name="tel" type="tel">
	Text
	<input name="text" type="text">
	<input type="submit">
</form>
<h1>Send batch SMS</h1>
<form action="gate" method="POST" name="batchForm">
    <input type="hidden" name="type" value="batch" />
	CSV
	<input type="file">
	Supports {} by col formatting
	<input type="text">
	<input type="submit">
</form>
</body>
</html>