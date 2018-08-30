			function query(idroom) {
				window.location.href="/tspace/User-mobile/roomdisplay.jsp?idroom="+idroom;
			}
			$("#myCarousel").swipeleft(function() {
				$(this).carousel('next');
			});
			$("#myCarousel").swiperight(function() {
				$(this).carousel('prev');
			});
			var nav0 = document.getElementById("nav0");
			nav0.setAttribute("class","ui-btn-active");