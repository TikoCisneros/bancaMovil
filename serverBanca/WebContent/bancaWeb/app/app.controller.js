function addMsg(tipo, titulo, mensaje)
{
	$('#msg').html('<div class="alert alert-'+tipo+' alert-dismissible" role="alert">'
			  +'<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>'
			  +'<strong>'+(titulo || "")+'</strong> <br>'
			  +mensaje
			  +'</div>');
	$('body').append();
	}

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
					console.log('Respuesta'+res);
					if (res.status == 'EA')
						addMsg('danger', res.value);
					else {
						console.log(res);
						UserCM.set(res.value);
						console.log(UserCM.get());
						$location.path('/main');
					}
				});
			};
		} ]);

bancaWebController.controller('redirectCtrl', [ '$location',
		function($location) {
			$location.path('/login');
		} ]);

bancaWebController.controller('passCtrl',[ '$scope','$location',
       'bancaWebSV', 'UserCM', 
       function($scope, $location, bancaWebSV, UserCM){
			//verificar usuario sesion
	
			$scope.mspass = function(){
				console.log('entra');
				bancaWebSV.spass({
					"pwd": $scope.pwd,
					"npd": $scope.npd,
					"cpd": $scope.cpd
				},function(res){
					console.log(res);
					addMsg('success', 'Sus datos han sido cambiados');//info success danger warning
					$scope.pwd=null;
					$scope.npd=null;
					$scope.cpd=null;
				});
			};
        }]);

bancaWebController.controller('mainCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {
			$scope.user = UserCM.get(); 
			console.log($scope.user);
		} 
]);