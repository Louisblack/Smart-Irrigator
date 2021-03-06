'use strict';

/* Services */

var smartIrrigatorWebServices = angular.module('smartIrrigatorWebServices', ['ngResource']);

smartIrrigatorWebServices.service('ExecutionService', ['$http',
    function($http){

        $http.defaults.useXDomain = true;

        this.getExecutions = function(pageNumber, callback) {
            // Hardcoded domain for test only
            $http.get('http://localhost:8888/executions/' + pageNumber).
                success(function(data){
                    callback(data);
                }).
                error(function(data) {

                });
        };

        this.getDetail = function(date, callback) {
            $http.get('http://localhost:8888/executions/detail/'+ date).
                success(function(data){
                    callback(data);
                }).
                error(function(data) {

                });
        };

    }
]);

smartIrrigatorWebServices.service('WaterNowService', ['$http',
    function($http){

        $http.defaults.useXDomain = true;

        this.waterNow = function(callback) {
            // Hardcoded domain for test only
            $http.post('http://localhost:8888/watering/', {
                seconds: window.prompt("How long should I water for?")
            }).
            success(callback).
            error(function(data) {

            });
        };

    }
]);
