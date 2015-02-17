/*
 * Copyright (c) 2015 Maxcode B.V.
 */

RequestMethod = {
    DELETE: "DELETE",
    PUT: "PUT",
    POST: "POST",
    GET: "GET"
};

ResponseType = {
    JSON: "json"
};

TT = new function()
{
    // initialized in commonJS.jsp
    this.CONTEXT_PATH = '';

    this.templateFill = function (template_, data_)
    {
        // #[some] - get value of [some]
        // #[some#?else] - get value of [some] only if it's value = [else]
        // #[some#?else#:this] - get [this] only if value of [some] = [else]

        return template_.replace(/#\[[a-z,0-9]{1,}(#\?){0,}[a-z,0-9]{0,}(#:){0,}[a-z,0-9]{1,}\]/gi, function (match)
        {
            var returnedValue;
            var equalsTo;
            var fieldName = match.replace("#[", '').replace("]", '');
            fieldName = fieldName.split('#?')[0];
            var fieldValue = data_[fieldName];

            if(fieldName == 'enabled')
            {
                console.log('fieldName');
            }

            if(match.indexOf('#?') > 0)
            {
                var pos;
                if(match.indexOf('#:') > 0)
                {
                    pos = match.indexOf('#:');
                    returnedValue = match.substring(pos+2, match.length-1);
                    equalsTo = match.substring(match.indexOf('#?')+2, pos);
                }
                else
                {
                    pos = match.indexOf('#?');
                    equalsTo = match.substring(pos+2, match.length-1);
                    returnedValue = equalsTo;
                }
            }
            else
            {
                equalsTo = fieldValue;
                returnedValue = fieldValue;
            }


            if (typeof fieldValue != 'undefined')
            {
                if(fieldValue == null)
                {
                    return null;
                }

                if(fieldValue.toString() == equalsTo.toString())
                {
                    return returnedValue;
                }
                else
                {
                    return '';
                }
            }

            return match;
        })
    };

    this.getTemplate = function (templateSelector_)
    {
        return $(templateSelector_).removeClass(templateSelector_).remove()[0].outerHTML;
    };

    this.sliceWord = function(string_, charLimit_)
    {
        if(string_.length > charLimit_)
        {
            return string_.substr(0, --charLimit_) + '..';
        }

        return string_;
    };

    /**
     * detects position of the element in dom
     * @param ele - javascript object, not jquery
     * @returns {Array}
     */
    this.getElementPosition = function (ele)
    {
        var x=0;
        var y=0;

        while(true)
        {
            x += ele.offsetLeft;
            y += ele.offsetTop;

            if(ele.offsetParent === null)
            {
                break;
            }
            ele = ele.offsetParent;
        }

        return [x, y];
    };
};

$.fn.serializeObject = function()
{
    var result = {};
    var data = this.serializeArray();
    $.each(data, function() {
        if (result[this.name] !== undefined) {
            if (!result[this.name].push) {
                result[this.name] = [result[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
            result[this.name] = this.value || '';
        }
    });

    // todo, add support for checkboxes

    return result;
};