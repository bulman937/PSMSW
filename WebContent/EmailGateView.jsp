<!DOCTYPE html>
<%@page pageEncoding="UTF-8" %>
<html>
<head>
<title>Email Gate</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
 $(function() {

  // We can attach the `fileselect` event to all file inputs on the page
  $(document).on('change', ':file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
  });

  // We can watch for our custom `fileselect` event like this
  $(document).ready( function() {
      $(':file').on('fileselect', function(event, numFiles, label) {

          var input = $(this).parents('.input-group').find(':text'),
              log = numFiles > 1 ? numFiles + ' files selected' : label;

          if( input.length ) {
              input.val(log);
          } else {
              if( log ) alert(log);
          }

      });
  });
  
});
</script>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">PicoSMS</a>
    </div>
    <ul class="nav navbar-nav">
      <li><a href="/test/sms">Send SMS</a></li>
      <li><a href="/test/email">Send Email</a></li>

    </ul>
    <ul class="nav navbar-nav navbar-right">
    <!-- Trigger the modal with a button -->
		<li><button type="button" class="btn btn-info btn-lg " data-toggle="modal" data-target="#myModal">Log-in</button><li>
		<li><form action="email" method="POST">
		<input name="type" type="hidden" value="logout">
		<button type="submit"  name="logout" class="btn btn-info btn-lg ">
		Log-out</button></form><li>
    </ul>
  </div>
</nav>
<div class="container">
<h1>Email Service</h1>
${buffer}
<h1>Send single Email</h1>

<form action="email" method="POST" name="singleForm">
<div class="form-group">
	<input name="type" type="hidden" value="single">
    <label for="email">Email:</label>
    <input name="email" class="form-control" 
    		id="phone" placeholder="Enter phone number" ${lock}>
    <label for="text">Text:</label>
    <textarea name="text" class = "form-control" rows = "3"></textarea>
    <br>
	<button type="submit" class="btn btn-success" ${lock}>Submit</button>
</div>
</form>

<h1>Send batch Email</h1>
<form action="email" method="POST" name="batchForm" 
					enctype="multipart/form-data" >
	<div class="form-group">
    <input type="hidden" name="type" value="batch" />
    <label class="control-label">Select File</label> 
            <div class="input-group">
                <label class="input-group-btn">
                    <span class="btn btn-success">
                        Browse&hellip; <input name="_file" type="file" 
                        style="display: none;" ${lock} multiple >
                    </span>
                </label>
                <input type="text" class="form-control"  readonly>
            </div>
    <label class="control-label">Topic</label>            

    <textarea name="topic" placeholder="topic" class = "form-control" rows = "1" ${lock} ></textarea>
    <br>
        <label class="control-label">Separator</label>            
    
    <input type="text" name="separator" class="form-control" id="separator" placeholder="Sepearator" ${lock}>
    <br>
    <label class="control-label">Select address colunum</label>
    <input name="target" class="form-control" id="target" placeholder="Describe target colonum name" ${lock}>     
    <br>
    <label class="control-label">Supports {} by col formatting</label>
    <br>
    <textarea name="text" class = "form-control" rows = "3" ${lock}></textarea>
    <br>
	<button type="submit" onclick="javascript:alert('Отправляется...')" class="btn btn-success" ${lock}>Submit</button>

</div>
</form>

</div>

<!-- Modal -->
<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Welcome!</h4>
      </div>
      <div class="modal-body">
        <form method="POST" name="login" action="email" >
        <div class="form-group">	
            <input type="hidden" name="type" value="login" />
            <label class="control-label">Login: </label> 
	        <input name="username" class="form-control" placeholder="Login">
	        <br>
            <label class="control-label">Password: </label> 
	        <input name="password" class="form-control" placeholder="Password">
	        <br>	
         	<button type="submit" class="btn btn-primary">Submit</button>        
        </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
 </div>
</body>
</html>
