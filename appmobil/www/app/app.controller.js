function addMsg(tipo, titulo, mensaje) {
	$.gritter.add({
		title : titulo,
		text : mensaje || 'Desconocido',
		image : 'img/' + tipo + '.png',
		position : 'top-left'
	});
}
var tiempoInactividadP = 30 * 1000;
var bancaWebController = angular.module('bancaWebController', []);
var sesion;
function setS(t)
{
	sesion = t;

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
				if(!data || data.length < 4)
				{
					nfail(1);
					$('#page').val('login');
				}
				else
				{
					//$scope.page = 'main';
					$location.search('page', 'main');
					$scope.user = 
					{
						id: data[0],
						ping: data[1],
						n: data[2],
						fc: data[3]
					};
					console.log(data);
					console.log(new Date().getTime());
					if(new Date().getTime() > data[3])
						$('#mPIN').modal({backdrop: 'static', keyboard: false}); 
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
						$scope.usr = "";
						$scope.pwd="";
						res = res.value;
						$scope.page = 'main';
						$location.search('page', 'main');
						$scope.user = res;
						var nf = new Date().getTime();
						nf+= tiempoInactividadP;//(15 * 24 * 60 *60 *1000);
						$scope.user.n = res.apellido +" "+res.nombre;
						$scope.user.fc = nf;
						$scope.txt = res.id +"|"+res.ping +'|'+ $scope.user.n 
						 +'|' + nf;
						sesion = $scope.txt;
						window.requestFileSystem(typefs	
							, 0,
							$scope.readSesion,fail);
						if (res.status == 'FL') {
							addMsg('info', 'Información', 'Favor cambiar el password.');
						}
					}
				});
			};
			$scope.toPage = function(p)
			{
				console.log('Cambiando a '+p);
				$scope.page = p;
				$location.search('page',p);
			}
			$scope.mlogout = function() 
			{
				console.log("Cerrando cuenta");
				var rmS = function ()
				{
					window.requestFileSystem(typefs , 0, function(fs)
					{
						console.log("File System");
						fs.root.getFile("banca.sesion", {create: true},
							function(fe)
							{
								fe.remove(function(){console.log("Sesion eliminada.");},
									function(){console.log("No se pudo eliminar el archivo");}
									);
							});
					}
					, fail);
				};
				if(!$scope.user || !$scope.user.id)
				{
					rmS();
					$('#page').val('login');
					$('#page').trigger('input');
					return;
				}
				$scope.page = 'load';
				$('#status').html('Cerrando cuenta ...');
				bancaWebSV.logout({
					"idc" : $scope.user.id,
				},function(res)
				{
					if(res.status != 'OK')
					{
						$scope.page = 'login';
						return addMsg('danger', 'Error', res.value);
					}
					rmS();
					$('#page').val('login');
					$('#page').trigger('input');
					$scope.user = "";
					return;
				});
			};
			$scope.setPIN = function() 
			{
				console.log("Cambiando de PIN");
				if(!$scope.PIN)
					return addMsg('danger' , 'Error', 'Escribe el PIN');
				$('#mPIN').modal('toggle');
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				bancaWebSV.sesion({
					"idc" : $scope.user.id,
					"p": $scope.PIN
				}, function(res)
				{
					if(res.status != 'OK')
					{
						$scope.mlogout();
						return addMsg('danger', 'Error', 'PIN invalido.');
					}
					$scope.user.ping = $scope.PIN;
					$scope.user.fc = new Date().getTime() + tiempoInactividadP;
					$scope.txt = $scope.user.id +"|"+$scope.user.ping +'|'+ $scope.user.n 
							 +'|' + $scope.user.fc;
					sesion = $scope.txt;
					window.requestFileSystem(typefs	
						, 0,
						$scope.readSesion,fail);
				});

			};
			$scope.setCuenta = function(v)
			{
				$scope.c = v;
			}
			$scope.getTrans = function() 
			{
				$scope.c = {};
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				$scope.page = 'load';
				$('#status').html('Obteniendo datos ...');
				bancaWebSV.getTrans({
					"idc" : $scope.user.id,
					"p": $scope.user.ping
				},function(res)
				{
					if(res.status != 'OK')
						{
						$scope.page = 'main';
						return addMsg('danger', 'Error', res.value);
					}
					if(res.value == 'NS')
						{
						$scope.page = 'login';
						return addMsg('danger', 'Error', 'No hay una sesión activa.');
					}
					$scope.page = "Transferencias";
					console.log(res.value);
					$scope.cuentas = res.value;
				});
			};
			$scope.getTranc = function() 
			{
				$scope.c = {};				
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				$scope.page = 'load';
				$('#status').html('Obteniendo datos ...');
				bancaWebSV.getTranc({
					"idc" : $scope.user.id,
					"p": $scope.user.ping
				},function(res)
				{
					if(res.status != 'OK')
					{
						$scope.page = 'main';
						return addMsg('danger', 'Error', res.value);
					}
					if(res.value == 'NS')
						{
						$scope.page = 'login';
						return addMsg('danger', 'Error', 'No hay una sesión activa.');
					}
					$scope.page = "Transacciones";
					console.log(res.value);
					$scope.cuentas = res.value;
				});
			};
			$scope.getMonto = function(i)
			{
				console.log("mostrando monto");
				$scope.monto = $scope.cuentas[i].monto;
			};
			$scope.getCuentas = function(tipo) 
			{
				$scope.monto = null;
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				$scope.page = 'load';
				$('#status').html('Obteniendo datos ...');
				bancaWebSV.getCuentas({
					"idc" : $scope.user.id,
					"p": $scope.user.ping
				},function(res)
				{
					console.log(res);
					if(res.status != 'OK')
						{
						$scope.page = 'main';
						return addMsg('danger', 'Error', res.value);
					}
					if(res.value == 'NS')
						{
						$scope.page = 'login';
						return addMsg('danger', 'Error', 'No hay una sesión activa.');
					}
					$scope.page = "Cuentas";
					console.log(res.value);
					if(tipo)
					{
						$scope.cuentas = [];
						for (var i = 0; i < res.value.length; i++) {
							if(res.value[i].tipo == tipo)
								$scope.cuentas.push(res.value[i]);
						};
					}else
						$scope.cuentas = res.value;
				});
			};
			$scope.inittrans = function()
			{
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				if(!$scope.user || !$scope.user.id)
				{
					$scope.mlogout();
					return addMsg('danger' , 'Error', 'No hay una sesion');
				}
				$scope.page = 'load';
				$('#status').html('Obteniendo datos ...');
				bancaWebSV.getCuentas({
					"idc" : $scope.user.id,
					"p": $scope.user.ping
				},function(res)
				{
					console.log(res);
					if(res.status != 'OK')
						{
						$scope.page = 'main';
						return addMsg('danger', 'Error', res.value);
					}
					if(res.value == 'NS')
						{
						$scope.page = 'login';
						return addMsg('danger', 'Error', 'No hay una sesión activa.');
					}
					$scope.page = "Transferir";
					console.log(res.value);
					$scope.cuentas = res.value;
				});

			}
			$scope.destino;
			$scope.existeDestino = true;
			$scope.validarDest = function() {
				console.log("Validando destino");
				if ($scope.destino && $scope.destino.length > 5) {
					bancaWebSV.vc({
						nro : $scope.destino
					}, function(res) {
						console.log("destino" +res.status);
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
						"idc" : $scope.user.id,
						"p": $scope.user.ping,
						nroD : $scope.destino,
						nroO : $scope.origen,
						monto : $scope.monto
					}, function(res) {
						$('#btntra').html('<i class="fa fa-send"></i> Transferir');
						$('#btntra').removeAttr('disabled');
						console.log(JSON.stringify(res));

						if (res.status != 'OK')
							addMsg('danger', 'Error', res.value || 'Cuenta destino invalida');
						else if(res.value == 'NS')
						{
							$scope.page = 'login';
							return addMsg('danger', 'Error', 'No hay una sesión activa.');
						}
						else {
							addMsg('info', 'Información', res.value);
							$('#page').val('main');
							$('#page').trigger('input');
						}

					});
				}
			};

	}]);