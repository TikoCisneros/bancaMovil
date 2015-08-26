function addMsg(tipo, titulo, mensaje) {
	$.gritter.add({
		title : titulo,
		text : mensaje || 'Desconocido',
		image : 'img/' + tipo + '.png',
		position : 'top-left'
	});
}
var bancaWebController = angular.module('bancaWebController', []);
var sesion;
function setS(t)
{
	sesion = t;

}
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

bancaWebController.controller('mobileCtrl', [ '$scope', '$location',
		'bancaWebSV', 
		function($scope, $location, bancaWebSV) {
			console.log("CONTROLADOR CARGADO");
			$scope.page = 'load';
			$location.search('page', 'login');
			$('.hide').removeClass('hide');
			$('#status').html('Preparando interfaz ...');
			$scope.readSesion = function(fs)
			{
				console.log("File System");
				console.log(sesion);
				fs.root.getFile("banca.sesion", {create: true},
					function(fe)
					{
						if(sesion)
						{
							fe.createWriter(function(w)
								{
									w.write(sesion);
									sesion = null;
								}, fail);
						}else
						{
							console.log("Buscando");
							fe.file(function(f)
								{
									console.log("Reading");
									readAsText(f);
									function readAsText(file) {
								        var reader = new FileReader();
								        reader.onloadend = function(evt) {
								            console.log("Read as text");
								            console.log(evt.target.result);
								            validarSesion(evt.target.result);
								        };
								        reader.readAsText(file);
								    }
								}, nfail);
						}
					}
					, fail);
			}
			function fail(evt)
				{
					console.log(evt);
					addMsg('danger', 'Error', 'Fallo en leer la sesión. Accede nuevamente.');
					$scope.page = 'login';
					$location.search('page', 'login');
				}
			function nfail(evt)
				{
					console.log(evt);
					$scope.page = 'login';
					$location.search('page', 'login');
				}
			function validarSesion(data)
			{
				data = data.split('|');
				if(!data || data.length < 3)
				{
					nfail(1);
					$('#page').val('login');
				}
				else
				{
					//$scope.page = 'main';
					$location.search('page', 'main');
					hayUser(data);
					$('#page').val('main');
				}
				$('#page').trigger('input');
			}
			var typefs = LocalFileSystem.PERSISTENT;
			window.requestFileSystem(typefs , 0,
				$scope.readSesion, fail);
			$scope.mlogin = function() {
				console.log('Haciendo login');
				$scope.page = 'load';
				$('#status').html('Haciendo login ...');
				bancaWebSV.login({
					"usr" : $scope.usr,
					"pwd" : $scope.pwd
				}, function(res) {
					console.log('Respuesta');
					if (res.status == 'EA')
						{
							addMsg('danger' , 'Error', res.value);
							$scope.page = 'login';
						}
					else {
						res = res.value;
						$scope.page = 'main';
						$location.search('page', 'main');
						$scope.user = res;
						$scope.txt = res.id +"|"+res.ping +'|'+new Date().getTime();
						sesion = $scope.txt;
						window.requestFileSystem(typefs	
							, 0,
							$scope.readSesion,fail);
						hayUser($scope.txt);
						if (res.status == 'FL') {
							addMsg('info', 'Información', 'Favor cambiar la contrasena');
						}
					}
				});
			};

		//verificar session

		//manejador si hay usuario
		function hayUser(user)
		{
			if(!user)
				return addMsg('warning', 'Error', 'No hay usuarios registrados.');
			console.log(user);
		}
	}]);
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
						addMsg('danger' , 'Error', res.value);
					else {
						console.log(res);
						UserCM.set(res.value);
						console.log(UserCM.get());
						if (res.status == 'FL') {
							addMsg('info', 'Información', 'Favor cambiar la contrasena');
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
					addMsg('info', 'Información', res.value);
					$location.path('verTransferencias');
				}

			});
		} ]);

bancaWebController.controller('validateRCtrl', [ '$scope', '$location',
	'$routeParams', 'bancaWebSV',
	function($scope, $location, $routeParams, bancaWebSV) {
		bancaWebSV.vregis({
			id : $routeParams.id,
			tk : $routeParams.tk
		}, function(res) {
			$routeParams = {};
			$location.search('id', null);
			$location.search('tk', null);
			if (res.status != 'OK') {
				addMsg('danger', 'Error', res.value);
				$location.path('main');
			} else {
				addMsg('info', 'Información', res.value);
				$location.path('login');
			}
	
		});
	} ]);

bancaWebController.controller('validateMCtrl', [ '$scope', '$location',
 	'$routeParams', 'bancaWebSV',
 	function($scope, $location, $routeParams, bancaWebSV) {
 		bancaWebSV.vsmail({
 			id : $routeParams.id,
 			ml : $routeParams.ml
 		}, function(res) {
 			$routeParams = {};
 			$location.search('id', null);
 			$location.search('ml', null);
 			if (res.status != 'OK') {
 				addMsg('danger', 'Error', res.value);
 			} else {
 				addMsg('info', 'Información', res.value);
 			}
 			$location.path('main');
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
						addMsg('danger', 'Error', res.value);// info success danger
						// warning
					} else {
						addMsg('success', 'Información', res.value);
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
						addMsg('danger', 'Error', res.value);// info success danger
						// warning
					} else {
						addMsg('success', 'Información', res.value);
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
						addMsg('danger', 'Error', res.value);// info success danger
						// warning
					} else {
						addMsg('success', 'Infromación', res.value);
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
						addMsg('danger', 'Error', res.value);// info success danger
						// warning
					} else {
						addMsg('success', 'Información', res.value);
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
				addMsg('success', 'Información', res.value);
				$location.path('/login');
			});
		};
		//Reenvio PIN
		$scope.newping = function(){
			bancaWebSV.rpincm(function(res) {
				if (res.status != 'OK'){
					addMsg('danger', 'Error', res.value);
				}else {
					addMsg('success', 'Información', res.value);
				}
			});
		};
		//Activar CM
		$scope.actcm = function(){
			bancaWebSV.actamov(function(res) {
				if (res.status != 'OK'){
					addMsg('danger', 'Error', res.value);
				}else {
					addMsg('success', res.value);
				}
			});
		};
		//Desactivar CM
		$scope.dsccm = function(){
			bancaWebSV.dctamov(function(res) {
				if (res.status != 'OK'){
					addMsg('danger', 'Error', res.value);
				}else {
					addMsg('success', 'Información', res.value);
				}
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
				$('#btntra').html('Procesando ...');
				$('#btntra').attr('disabled', 'true');
				if ($scope.existeDestino) {
					bancaWebSV.transferencia({
						nroD : $scope.destino,
						nroO : $scope.origen,
						monto : $scope.monto
					}, function(res) {
						$('#btntra').html('<i class="fa fa-send"></i> Transferir');
						$('#btntra').removeAttr('disabled');
						console.log(res);
						if (res.status != 'OK')
							addMsg('danger', 'Error', res.value);
						else {
							addMsg('info', 'Información', res.value);

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
			$scope.setCuenta = function(c)
			{
				$scope.c = c;
			};
			bancaWebSV.getTrans(function(res) {
				console.log(res);
				if (res.status != 'OK')
					return addMsg('danger', 'Error', res.value);
				else {
					$scope.cuentas = res.value;
				}
			});
		} ]);

bancaWebController.controller('lstTrancCtrl', [ '$scope', '$location',
		'bancaWebSV', 'UserCM',
		function($scope, $location, bancaWebSV, UserCM) {

			$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
				$scope.user = res;
			});
			$scope.cuentas;
			$scope.order;
			$scope.setCuenta = function(c)
			{
				$scope.c = c;
			};
			bancaWebSV.getTranc(function(res) {
				console.log(res);
				if (res.status != 'OK')
					return addMsg('danger', 'Error', res.value);
				else {
					$scope.cuentas = res.value;
				}
			});
		} ]);

bancaWebController.controller('ctamovCtrl', [ '$scope', '$location', 'bancaWebSV', 'UserCM',
 		function($scope, $location, bancaWebSV, UserCM) {
		$scope.user = verUser(UserCM, bancaWebSV, $location, function(res) {
			$scope.user = res;
		});
		//NUEVA CUENTA
 			$scope.mncm = function() {
 				bancaWebSV.nctamov({
 					"mpwd" : $scope.mp,
 					"mpwdc" : $scope.mpc,
 				}, function(res) {
 					console.log(res);
 					if (res.status == 'EA') {
 						addMsg('danger', 'Error', res.value);
 					} else {
 						addMsg('success', 'Información', res.value);
 						$scope.mp = null;
 						$scope.mpc = null;
 						$location.path('/main');
 					}
 				});
 			};
 			//CAMBIO PASS
 			$scope.mcpcm = function() {
 				bancaWebSV.pctamov({
 					"mpwd" : $scope.mpa,
 					"npwd" : $scope.mpn,
 					"cpwd" : $scope.mpcn,
 				}, function(res) {
 					console.log(res);
 					if (res.status == 'EA') {
 						addMsg('danger', 'Error', res.value);
 					} else {
 						addMsg('success', 'Información', res.value);
 						$scope.mpa = null;
 						$scope.mpn = null;
 						$scope.mpcn = null;
 						$location.path('/main');
 					}
 				});
 			};
 		} ]);