var taskApp = angular.module('myFacebookApp',['ngResource','checklist-model']);



taskApp.controller('myController', function($scope, $http, filterFilter){
		$scope.npages_a = [];
		var start;
		var end;
		var listSize;
		$scope.getall = function(){
			
			$http.get( '/get-all-pages',{})
			.then(function(res){
				$scope.npages = [];
				//console.log(res.data);
				$scope.npages = res.data;
				//console.log($scope.npages);
				listSize = $scope.npages.length;
				console.log(listSize);
				//angular.forEach(res.data, function(value, key) {
				//	$scope.npages.push(value);
				//});
				$scope.first();
			}, function(error){
				
			}
		);
	}
		
		$scope.searchPage = function(){
			$http.post( '/search-page', {searchname: $scope.search_name})
			.then(function(res){
				document.getElementById("search_name").value = '';
				document.getElementById("page_name").value = '';
				$scope.npages = [];
				console.log(res.data);
				$scope.npages = res.data;
				/*angular.forEach(res.data, function(value, key) {
					$scope.npages.push(value);
				});*/
				listSize = $scope.npages.length;
				console.log(listSize);
				
				//document.getElementById("results").value = 'Found '+listSize+ ' results for '+$scope.search_name+'.';
				$scope.first();
			}, function(error){
				
			}
		);
	}	
		
		$scope.addPage = function() {
			$http.post( '/add-pages',{pagename: $scope.page_name})
		
				.then(function(res){
					alert("Successfully added "+$scope.page_name+" !");
					document.getElementById("page_name").value = '';
					$scope.getall();
		//			console.log(res.data);
					//$scope.npages.push(res.data);
					
				}, function(error) {

					}	
				);
	}

		$scope.delPage = function(page, $index){
			$http.get( '/del-page', {params:{"pageid": page.id}})
			.then(function(res){
				alert("Successfully deleted "+page.pname+" !");
				$scope.npages.splice($scope.npages.indexOf(page),1);
				//console.log($scope.npages);
				$scope.npages_a = [];
				$scope.npages_a = $scope.npages.slice(start,end);
				//console.log($scope.npages_a);
			  }, function(error){
				
			}
		);
	}
		
		$scope.first = function() {
			start = 0;
			if(listSize<10) {
				end = listSize;
			}
			else {
				end = 10;
			}
			$scope.npages_a = $scope.npages.slice(start,end);				
	};
	
		$scope.next = function() {
			if(end < listSize){
			start = end;
			end = start + 10;
			$scope.npages_a = $scope.npages.slice(start,end);				
			}
	};
		
		$scope.prev = function() {
			if(start > 0){
			end = start;
			start = start - 10;
			$scope.npages_a = $scope.npages.slice(start,end);				
			}
	};
});


taskApp.controller('selectController', function($scope, $http, filterFilter, $window){
	$scope.npages_a = [];
	var start;
	var end;
	var listSize;
	$scope.appAccessToken = '';
	$scope.getall = function(token){
		$scope.appAccessToken = token;		
		//console.log(token);
		$http.get( '/get-all-pages',{})
		.then(function(res){
			$scope.npages = [];
			//console.log(res.data);
			$scope.npages = res.data;
			//console.log($scope.npages);
			listSize = $scope.npages.length;
			console.log(listSize);
			$scope.first();
			document.getElementById("fromDate").value = '';
			document.getElementById("toDate").value = '';
		}, function(error){
			
		}
	);
}
	
	$scope.searchPage = function(){
		$http.post( '/search-page', {searchname: $scope.search_name})
		.then(function(res){
			document.getElementById("search_name").value = '';
			
			console.log(res.data);
			$scope.npages = [];
			angular.forEach(res.data, function(value, key) {
				$scope.npages.push(value);});
				listSize = $scope.npages.length;
				//console.log(listSize);
				$scope.first();
			
		}, function(error){
			
		}
	);
}	
	
	$scope.first = function() {
		start = 0;
		if(listSize<10) {
			end = listSize;
		}
		else {
			end = 10;
		}
		$scope.npages_a = $scope.npages.slice(start,end);
		//console.log($scope.npages_a);
		
};
	
	$scope.next = function() {
		if(end < listSize){
		start = end;
		end = start + 10;
		$scope.npages_a = $scope.npages.slice(start,end);
		}
};
	
	$scope.prev = function() {
		if(start > 0){
		end = start;
		start = start - 10;
		$scope.npages_a = $scope.npages.slice(start,end);
		}
};

	
	// checkbox page start
	$scope.npages_n = [];
	$scope.insertPage = function(pagename, flag){
		
		var sbmt = document.getElementById("submit");
		//console.log(page);
		//console.log(flag);
		if(flag == true){
			$scope.npages_n.push(pagename);
			console.log($scope.npages_n);
		}else{
			$scope.npages_n.splice($scope.npages_n.indexOf(pagename),1);
			console.log($scope.npages_n);
		}
		if($scope.npages_n.length != 0){
			sbmt.disabled = false;
		}
		if($scope.npages_n.length == 0){
			sbmt.disabled = true;
		}
		//console.log($scope.npages_n)
	}
	
	$scope.submitPages = function(fromDate, toDate){
		var fDate = document.getElementById('fromDate').value;
		var tDate = document.getElementById('toDate').value;
	//	$scope.npages_n.push(toDate);
	//	$scope.npages_n.push(fromDate);
		
		if(!fDate){
			$scope.npages_n.push("0");	
			}else{
				$scope.npages_n.push(fromDate);
			}
		if(!tDate){
			$scope.npages_n.push("0");	
			}else{
				$scope.npages_n.push(toDate);
			}
					
		console.log($scope.npages_n);
		$scope.npages_c = $scope.npages_n.toString();
		console.log($scope.npages_c);
		
		//validate token ... 
		$http.get("https://graph.facebook.com/v2.0/me?access_token=" + $scope.appAccessToken).then(function(response) {
			$.fileDownload('facebook/posts/download', {
		        preparingMessageHtml: "Please wait...",
		        failMessageHtml: "There was a problem generating your report, please try again.",
		        httpMethod: "POST",
		        data: {spages: $scope.npages_c, accessToken: $scope.appAccessToken },
		        successCallback: function(url) {
		        	//$window.location.href = 'http://localhost:9000/facebook/posts'
		        	$window.location.href = 'http://178.79.182.229:7070/facebook/posts'
		        }
		        
		    })
		}, function(response) {
			
			
			
		});
		
		
		
		

		/*$http.post( 'facebook/posts/download', {spages: $scope.npages_c, accessToken: $scope.appAccessToken })
		.then(function(data, status, headers){
			var contentType  = "application/octet-stream";
			var blob = new Blob([data], { type: contentType });
			var urlCreator = window.URL || window.webkitURL || window.mozURL || window.msURL;
               
            var url = urlCreator.createObjectURL(blob);
            window.location = url;
			var sbmt = document.getElementById("submit");
			sbmt.disabled = true;
			fDate = '';
			tDate = '';
			$scope.getall($scope.appAccessToken);
			$scope.npages_n = [];
		}, function(error){
			
			var sbmt = document.getElementById("submit");
			sbmt.disabled = true;
			fDate = '';
			tDate = '';
			$scope.getall($scope.appAccessToken);
			$scope.npages_n = [];
			
		}
	);*/
}
	//checkbox page end
	
});

