<jsp:include page="header.jsp" />
<div class="page-history">
	<div class="second-header">
		<h1>Camera 1 - History</h1>
	</div>
	<div class="main-content">
		<div class="current-picture big-picture">
			<div class="row">
				<div class="col-md-12">
					<div class="image-wrapper">
						<img class="camerapi-image" src="/cameraPi_server/photos/last.jpg">
						<canvas class="camerapi-canvas"></canvas>
						<span class="img-title"></span>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6 text-right">
					<span class="quicksel qs-minus-20f text-center"> &#9194;&#9194; </span>
					<span class="quicksel qs-minus-5f text-center hidden-xs hidden-sm"> &#9194; </span>
					<span class="quicksel qs-minus-1f text-center"> &#8592; </span>
				</div>
				<div class="col-xs-6 text-left">
					<span class="quicksel qs-plus-1f text-center"> &#8594; </span>
					<span class="quicksel qs-plus-5f text-center hidden-xs hidden-sm"> &#9193; </span>
					<span class="quicksel qs-plus-20f text-center"> &#9193;&#9193; </span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 text-center">
					<div class="current-thumbs">
					</div>
				</div>
			</div>
		</div>
		
		<!-- date picker -->
		<div class="row">
			<div class="col-md-6 text-center">
				<fieldset>
					<label>Date</label><br>
					 <input type="text" class="history-datepicker">
				</fieldset>
			</div>
			<div class="col-md-6 text-center">
				<fieldset>
					<label>Hour</label><br>
					<input type="number" class="history-hour" min="0" max="24" value="13">
				</fieldset>
			</div>
		</div>
		<br><br>
		<div class="clr"></div>
		<div class="row">
			<div class="col-md-12 text-center">
			<div class="hours-thumb-container"></div>
			</div>
		</div>
		<br>
	</div>
</div>
<jsp:include page="footer.jsp" />
