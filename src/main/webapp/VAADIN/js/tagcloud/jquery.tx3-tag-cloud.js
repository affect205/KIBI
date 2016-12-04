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
        $.each(element.find("li"), function () {
            var dataWeight = getDataWeight(this);
            var percent = Math.abs((dataWeight - lDataWeight) / (lDataWeight - hDataWeight));
            $(this).css('font-size', (1 + (percent * settings['multiplier'])) + "em");
        });

    }

    function getDataWeight(element) {
        return parseInt($(element).attr("tag-weight"));
    }

    function logWarning(message) {
        console.log("[WARNING] " + Date.now() + " : " + message);
    }
}(jQuery));
