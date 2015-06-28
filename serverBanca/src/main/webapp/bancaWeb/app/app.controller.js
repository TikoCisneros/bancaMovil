var bancaWebController = angular.module('bancaWebController', []);

bancaWebController.controller('loginCtrl',['$scope','bancaWebSV',
	function($scope,bancaWebSV){
	
		
		
		
		$scope.mlogin = function ()
		{
			
			bancaWebSV.login({usr: $scope.usr, pwd: $scope.pwd},function(res)
				{
					if(res.code == 'EA')
						alert(res);
					else
					{
						alert(res);
					}
				});
		}
	}
]);