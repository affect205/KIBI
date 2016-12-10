(function ($) {
    var settings;
    $.fn.tx3TagCloud = function (options) {
        console.log("tag cloud main func...");
        settings = $.extend({
            multiplier: 2.5
        }, options);
        main(this);
    };

    function main(element) {
        element.addClass("tx3-tag-cloud");
        addListElementFontSize(element);
    }

    function addListElementFontSize(element) {
        $.each(element.find("li"), function () {
            var percent = 0.42;
            var dataWeight = getDataWeight(this);
            if (dataWeight != undefined && !isNaN(dataWeight) && dataWeight > 1) {
                if (dataWeight > 2) {
                    percent = Math.log(dataWeight) - Math.log10(dataWeight);
                } else percent = 0.56;
            }
            var fontSize = percent * settings['multiplier'];
            console.log("percent: " + percent + ", fontSize: " + fontSize + ", tag: " + $(this).text() + ", weight: " + dataWeight);
            $(this).css('font-size', fontSize + "em");
        });
    }

    function getDataWeight(element) {
        return parseInt($(element).attr("tag-weight"));
    }
}(jQuery));
