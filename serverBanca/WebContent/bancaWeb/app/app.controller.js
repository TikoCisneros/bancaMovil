var bancaWebController = angular.module('bancaWebController', []);

bancaWebController.controller('loginCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			console.log('Cotrolador cargado');

			$scope.mlogin = function() {
				console.log('Haciendo login');
				bancaWebSV.login({
					"usr" : $scope.usr,
					"pwd" : $scope.pwd
				}, function(res) {
					console.log('Respuesta');
					if (res.code == 'EA')
						alert(res);
					else {
						console.log(res.value);
						UserCM.set(res.value);
						console.log(UserCM.get());
						$location.path('/main');
					}
				});
			}
		} ]);

bancaWebController.controller('redirectCtrl', [ '$location',
		function($location) {
			$location.path('/login');
		} ]);

bancaWebController.controller('mainCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			$scope.user = UserCM.get(); 
			console.log = $scope.user;
		} 
]);