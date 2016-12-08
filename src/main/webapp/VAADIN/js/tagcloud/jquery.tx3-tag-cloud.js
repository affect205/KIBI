(function ($) {
    var settings;
    $.fn.tx3TagCloud = function (options) {
        console.log("tag cloud main func...");
        settings = $.extend({
            multiplier: 1
        }, options);
        main(this);

    }

    function main(element) {
        element.addClass("tx3-tag-cloud");
        addListElementFontSize(element);
    }

    function addListElementFontSize(element) {
        var hDataWeight = -9007199254740992;
        var lDataWeight = 9007199254740992;
        $.each(element.find("li"), function () {
            cDataWeight = getDataWeight(this);
            if (cDataWeight == undefined) {
                logWarning("No \"tag-weight\" attribut defined on <li> element");
            } else {
                hDataWeight = cDataWeight > hDataWeight ? cDataWeight : hDataWeight;
                lDataWeight = cDataWeight < lDataWeight ? cDataWeight : lDataWeight;
            }
        });
        console.log("hDataWeight: " + hDataWeight);
        console.log("lDataWeight: " + lDataWeight);
        $.each(element.find("li"), function () {
            var dataWeight = getDataWeight(this);
            var dWeight = (hDataWeight == lDataWeight) ? 1 : lDataWeight - hDataWeight;
            console.log("dWeight: " + dWeight);
            var percent = Math.abs(((dataWeight+1) - lDataWeight) / dWeight);
            console.log("percent: " + percent);
            var fontSize = 1 + (percent * settings['multiplier']);
            console.log("fontSize: " + fontSize);
            $(this).css('font-size', fontSize + "em");
        });

    }

    function getDataWeight(element) {
        return parseInt($(element).attr("tag-weight"));
    }

    function logWarning(message) {
        console.log("[WARNING] " + Date.now() + " : " + message);
    }
}(jQuery));
