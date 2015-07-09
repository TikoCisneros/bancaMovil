function addMsg(tipo, titulo, mensaje)
{
	$.gritter.add({
		  title: titulo,
		  text: mensaje,
		  image:'/serverBanca/resources/img/'+tipo+'.png',
		  position:'top-left'
		});
	}

var bancaWebController = angular.module('bancaWebController', []);

function verUser(UserCM, bancaWebSV, location, callback)
{
	var user = UserCM.get();
	if(user && user.ci)
		return user;
	else
	{
		bancaWebSV.sesion(function(res)
		{
			if(res.status != 'OK')
			{
				addMsg('danger', 'Error', res.value);
				location.path('/login');
			}
				
			UserCM.set(res.value);
			callback(res.value);
		});
		
	}
}

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
		} 
]);

bancaWebController.controller('passCtrl',[ '$scope','$location',
       'bancaWebSV', 'UserCM', 
       function($scope, $location, bancaWebSV, UserCM){
			//verificar usuario sesion
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res)
			{
				$scope.user = res;
			});
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
		
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res)
					{
						$scope.user = res;
					});
		} 
]);