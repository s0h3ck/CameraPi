<jsp:include page="header.jsp" />
<div class="page-live">
  <div class="second-header">
    <h1>Camera 1 - Live</h1>
  </div>
  <div class="main-content row">
    <div class="col-md-6">
      <div class="camera-preview">
        <div class="camera-backdrop text-center">
          <div class="camera-and-buttons">
          	<div class="image-wrapper">
				<img alt='current image' class="camerapi-image" src="/cameraPi_server/photos/last.jpg">
            	<canvas class="camerapi-canvas"></canvas>
				<span class="img-title"></span>
			</div>
            <div class="row camera-buttons">
              <div class="mv-buttons">
                <div class="row">
                  <div class="col-xs-6">
                    <div class="row">
                      <div class="col-xs-12 text-left">
                        <button data-action="mv-left" class="circle-btn">
                          &#8592;
                        </button>
                      </div>
                    </div>
                  </div>
                  <div class="col-xs-6">
                    <div class="row">
                      <div class="col-xs-12 text-right">
                        <button data-action="mv-right" class="circle-btn">
                          &#8594;
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-6">
              <div class="camera-text">
                Universite de Sherbrooke <br> camera 1
              </div>
            </div>
            <div class="col-md-6">
              <div class="camera-time">
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="footer.jsp" />
