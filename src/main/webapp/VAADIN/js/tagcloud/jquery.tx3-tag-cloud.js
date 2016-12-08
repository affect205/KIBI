(function ($) {
    var settings;
    $.fn.tx3TagCloud = function (options) {
        console.log("tag cloud main func...");
        settings = $.extend({
            multiplier: 1
        }, options);
        main(this);
    };

    function main(element) {
        element.addClass("tx3-tag-cloud");
        addListElementFontSize(element);
    }

    function addListElementFontSize(element) {
        $.each(element.find("li"), function () {
            var percent = 0.5;
            var dataWeight = getDataWeight(this);
            if (dataWeight != undefined && dataWeight != NaN && dataWeight > 1) {
                if (dataWeight > 2) {
                    var log = Math.log(dataWeight);
                    percent = log - (log/6);
                } else percent = 0.75;
            }
            var fontSize = percent * settings['multiplier'];
            console.log("fontSize: " + fontSize);
            $(this).css('font-size', fontSize + "em");
        });
    }

    function getDataWeight(element) {
        return parseInt($(element).attr("tag-weight"));
    }
}(jQuery));
