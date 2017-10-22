module.exports = {
	templateUrl : "/assets/templates/nvmonitor.plugin.html",

	controller : function($scope, $websocket, $document, buildWSURL) {
		$scope.gauges = [];

		$scope.ws = $websocket(buildWSURL("/nvmonitor"));

		$scope.ws.onOpen(function() {
			$scope.ws.reconnectIfNotNormalClose = true;
		});

		$scope.ws.onMessage(function(message) {
			if (message && message.data) {
				var mon = angular.fromJson(message.data);
				if ($scope.monitor === undefined) {
					$scope.monitor = mon;
				} else {
					angular.merge($scope.monitor, mon);
				}

				mon.entries.forEach(function(entry) {
					var idx = $scope.getAttrib(entry, "gpu").value;
					[ "enc", "dec", "mem", "temp" ].forEach(function(gt) {
						var val = $scope.getAttrib(entry, gt);

						var elm = $document[0].getElementById(gt + "-" + idx);
						if (elm) {
							var gauge = $scope.gauges[gt + "-" + idx];
							if (gauge === undefined) {
								$scope.gauges[gt + "-" + idx] = Gauge($document[0].getElementById(gt + "-" + idx), {
									dialStartAngle : gt == "mem" || gt == "temp" ? 180 : 135,
									dialEndAngle : gt == "mem" || gt == "temp" ? 0 : 45,
									max : val.unit === "C" ? 120 : val.unit === "F" ? 250 : 100,
									value : val.value,
									label : function(value) {
										return Math.round(value) + (val.unit === "C" || val.unit === "F" ? "°" : " ") + val.unit;
									}
								});
							} else {
								gauge.setValueAnimated(val.value, 0.75);
							}
						}
					});
				});
			}
		});

		$scope.getAttrib = function(entry, name) {
			var ret = {};
			entry.attribs.forEach(function(attr) {
				if (attr.name === name) {
					ret = attr;
					return;
				}
			});
			return ret;
		};

	}
};