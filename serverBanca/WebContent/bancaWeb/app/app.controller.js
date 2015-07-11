function addMsg(tipo, titulo, mensaje) {
	$.gritter.add({
		title : titulo,
		text : mensaje || 'Desconocido',
		image : '/serverBanca/resources/img/' + tipo + '.png',
		position : 'top-left'
	});
}

var bancaWebController = angular.module('bancaWebController', []);

function verUser(UserCM, bancaWebSV, location, callback) {
	var user = UserCM.get();
	if (user && user.ci)
		return user;
	else {
		bancaWebSV.sesion(function(res) {
			if (res.status != 'OK') {
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
					console.log('Respuesta' + res);
					if (res.status == 'EA')
						addMsg('danger', res.value);
					else {
						console.log(res);
						UserCM.set(res.value);
						console.log(UserCM.get());
						if (res.status == 'FL') {
							addMsg('info', 'Favor cambiar la contrasena');
						}
						$location.path('/main');
					}
				});
			};

		} ]);

bancaWebController.controller('validateTCtrl', [ '$scope', '$location',
		'$routeParams', 'bancaWebSV', 'UserCM',
		function($scope, $location, $routeParams, bancaWebSV, UserCM) {
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			bancaWebSV.vtransferencia({
				t : $routeParams.t,
				tk : $routeParams.tk
			}, function(res) {
				$routeParams = {};
				$location.search('t', null);
				$location.search('tk', null);
				if (res.status != 'OK') {
					addMsg('danger', 'Error', res.value);
					$location.path('main');
				} else {
					addMsg('info', 'Informaci�n', res.value);
					$location.path('verTransferencias');
				}

			});
		} ]);
bancaWebController.controller('logOutCtrl', [ '$location', 'bancaWebSV',
		'UserCM', function($location, bancaWebSV, UserCM) {
			UserCM.set(null);
			bancaWebSV.logout(function(res) {
				if (res.status != 'OK')
					addMsg('danger', 'Error', res.value);
				else
					$location.path('/login');
			});
		} ]);

bancaWebController.controller('passCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {
			// verificar usuario sesion
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.mspass = function() {
				console.log('entra');
				bancaWebSV.spass({
					"pwd" : $scope.pwd,
					"npd" : $scope.npd,
					"cpd" : $scope.cpd
				}, function(res) {
					console.log(res);
					if (res.status == 'EA') {
						addMsg('danger', res.value);// info success danger
						// warning
					} else {
						addMsg('success', res.value);
						$scope.pwd = null;
						$scope.npd = null;
						$scope.cpd = null;
					}
				});
			};
		} ]);

bancaWebController.controller('regCtrl', [ '$scope', '$location', 'bancaWebSV',
		function($scope, $location, bancaWebSV, UserCM) {
			$scope.mrgs = function() {
				console.log('entra reg');
				bancaWebSV.regis({
					"ced" : $scope.ci,
					"mal" : $scope.correo,
					"als" : $scope.alias
				}, function(res) {
					console.log(res);
					if (res.status == 'EA') {
						addMsg('danger', res.value);// info success danger
						// warning
					} else {
						addMsg('success', res.value);
						$scope.ci = null;
						$scope.correo = null;
						$scope.alias = null;
						$location.path('/login');
					}
				});
			};
		} ]);

bancaWebController.controller('mailCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {
			// verificar usuario sesion
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.msmail = function() {
				console.log('entra');
				bancaWebSV.smail({
					"mail" : $scope.mail
				}, function(res) {
					console.log(res);
					if (res.status == 'EA') {
						addMsg('danger', res.value);// info success danger
						// warning
					} else {
						addMsg('success', res.value);
						$scope.mail = null;
					}
				});
			};
		} ]);

bancaWebController.controller('desmovCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {
			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});

			$scope.mdesmov = function() {
				bancaWebSV.desmov({
					"mt" : $scope.mtv
				}, function(res) {
					console.log(res);
					if (res.status == 'EA') {
						addMsg('danger', res.value);// info success danger
						// warning
					} else {
						addMsg('success', res.value);
						$scope.mtv = null;
					}
				});
			};
		} ]);

bancaWebController.controller('mainCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});

			$scope.logout = function() {
				bancaWebSV.logout(function(res) {
					UserCM.set(null);
					addMsg('success', res.value);
					$location.path('/login');
				});
			};
		} ]);
bancaWebController.controller('cuentasCtrl', [ '$scope', '$location',
		'$routeParams', 'bancaWebSV', 'UserCM',
		function($scope, $location, $routeParams, bancaWebSV, UserCM) {

			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.tipo = $routeParams.tipo;
			$scope.cuentas = [];
			bancaWebSV.getCuentas(function(res) {
				if (res.status != 'OK')
					return addMsg('danger', 'Error', res.value);
				else {
					console.log(res.value);
					$.each(res.value, function(i, v) {
						if (v.tipo == $scope.tipo || $scope.tipo == 'todas')
							$scope.cuentas.push(v);
					});
				}
			});

			$scope.getMonto = function(index) {
				$scope.monto = $scope.cuentas[index].monto;
			};

		} ]);
bancaWebController.controller('transCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.cuentas;
			$scope.destino;
			$scope.existeDestino = true;
			bancaWebSV.getCuentas(function(res) {
				if (res.status != 'OK')
					return addMsg('danger', 'Error', res.value);
				else {
					$scope.cuentas = res.value;
				}
			});

			$scope.validarDest = function() {
				if ($scope.destino && $scope.destino.length > 5) {
					bancaWebSV.vc({
						nro : $scope.destino
					}, function(res) {
						if (res.status != 'OK')
							$scope.existeDestino = false;
						else
							$scope.existeDestino = true;
					});
				}
			};
			$scope.trans = function() {
				if ($scope.existeDestino) {
					bancaWebSV.transferencia({
						nroD : $scope.destino,
						nroO : $scope.origen,
						monto : $scope.monto
					}, function(res) {
						console.log(res);
						if (res.status != 'OK')
							addMsg('danger', 'Error', res.value);
						else {
							addMsg('info', 'Informaci�n', res.value);

						}

					});
				}
			};
		} ]);
bancaWebController.controller('lstTransCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.cuentas;
			$scope.order;
			bancaWebSV.getTrans(function(res) {
				console.log(res);
				if (res.status != 'OK')
					return addMsg('danger', 'Error', res.value);
				else {
					$scope.cuentas = res.value;
				}
			});
		} ]);