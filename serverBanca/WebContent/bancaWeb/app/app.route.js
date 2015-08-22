var bancaWebApp= angular.module('bancaWebApp', ['ngRoute','bancaWebController','bancaWebServices']);
var svr = '/BancaWM'; // cambiar x tu path
bancaWebApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : svr + '/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/login', {
		templateUrl : svr + '/bancaWeb/login.html',
		controller : 'loginCtrl'
	}).when('/main', {
		templateUrl : svr + '/bancaWeb/main.html',
		controller : 'mainCtrl'
	}).when('/cpwd', {
		templateUrl : svr + '/bancaWeb/cpwd.html',
		controller : 'passCtrl'
	}).when('/cmail', {
		templateUrl : svr + '/bancaWeb/cmail.html',
		controller : 'mailCtrl'
	}).when('/dmovil', {
		templateUrl : svr + '/bancaWeb/dmovil.html',
		controller : 'desmovCtrl'
	}).when('/rgusr', {
		templateUrl : svr + '/bancaWeb/rgusr.html',
		controller : 'regCtrl'
	}).when('/logout', {
		templateUrl : svr + '/bancaWeb/main.html',
		controller : 'logOutCtrl'
	}).when('/cuentas/:tipo', {
		templateUrl : svr + '/bancaWeb/cuentas.html',
		controller : 'cuentasCtrl',
		reloadOnSearch: false
	}).when('/transferencia', {
		templateUrl : svr + '/bancaWeb/transferencia.html',
		controller : 'transCtrl'
	}).when('/validateR', {
		templateUrl : svr + '/bancaWeb/main.html',
		controller : 'validateRCtrl'
	}).when('/verTransferencias', {
		templateUrl : svr + '/bancaWeb/lstTrans.html',
		controller : 'lstTransCtrl'
	}).when('/verTransacciones', {
		templateUrl : svr + '/bancaWeb/lstTranc.html',
		controller : 'lstTrancCtrl'
	}).when('/validateT', {
		templateUrl : svr + '/bancaWeb/main.html',
		controller : 'validateTCtrl'
	}).when('/validateM', {
		templateUrl : svr + '/bancaWeb/main.html',
		controller : 'validateMCtrl'
	}).when('/regCM', {
		templateUrl : svr + '/bancaWeb/regcm.html',
		controller : 'ctamovCtrl'
	}).when('/pwdCM', {
		templateUrl : svr + '/bancaWeb/pwdcm.html',
		controller : 'ctamovCtrl'
	});
} ]);

