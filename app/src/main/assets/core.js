(function(){if(!window.INJECTION_FLAG){String.prototype.includes=String.prototype.includes||function(element){return this.indexOf(element)>=0};String.prototype.startsWith=String.prototype.startsWith||function(prefix){return this.indexOf(prefix)===0};String.prototype.endsWith=String.prototype.endsWith||function(suffix){return this.indexOf(suffix,this.length-suffix.length)>=0};Array.prototype.includes=Array.prototype.includes||function(element){return this.indexOf(element)>-1};window.queryString2JSON=function(data){var res={},pars=data.split('&'),kv,k,v;for(var index in pars){kv=pars[index].split('='),k=kv[0],v=kv[1],res[k]=decodeURIComponent(v)}return res};window.checkAndFormatUrl=function(url){return(url&&!url.startsWith('blob:')&&!url.startsWith('data:')&&url.length>20)?(url.startsWith('http')?url:(url.startsWith('//')?'http:'+url:'http://'+url)):''};window.parentNodeFilter=function(node,tagName,attribute,filterValue){if(node){node=node.parentNode,tagName=tagName.toUpperCase();if(node.tagName==='HTML'){return null}else if(node.tagName===tagName&&(!attribute||node.getAttribute(attribute)===filterValue)){return node}else{return parentNodeFilter(node,tagName,attribute,filterValue)}}return null};window.middleSubString=function(text,start,end){if(text&&text.includes(start)&&text.substring(text.indexOf(start)).includes(end)){start=(text.indexOf(start)+start.length);return text.substring(start,text.indexOf(end,start))}return''};window.getQueryString=function(link,name){var reg=new RegExp('(^|&)'+name+'=([^&]*)(&|$)','i'),r=((link.split('?'))[1]).match(reg);if(r!=null){return unescape(r[2])}return null};window.HookAjax=function(proxy){window.XHRFunctionArray=['open','setRequestHeader','send','abort','getResponseHeader','getAllResponseHeaders','overrideMimeType','addEventListener','removeEventListener','dispatchEvent'];window.NXMLHttpRequest=window.NXMLHttpRequest||XMLHttpRequest;XMLHttpRequest=function(){Object.defineProperty(this,'xhr',{value:new window.NXMLHttpRequest})};for(var attribute in window.NXMLHttpRequest.prototype){if(XHRFunctionArray.includes(attribute)){XMLHttpRequest.prototype[attribute]=hookFun(attribute)}else{Object.defineProperty(XMLHttpRequest.prototype,attribute,{get:getFactory(attribute),set:setFactory(attribute),enumerable:true})}}function getFactory(attribute){return function(){var v=this.hasOwnProperty(attribute+"_")?this[attribute+"_"]:this.xhr[attribute];var attributeGetterHook=(proxy[attribute]||{})["getter"];return attributeGetterHook&&attributeGetterHook(v,this)||v}}function setFactory(attribute){return function(v){var xhr=this.xhr,that=this,proxyFun=proxy[attribute];if(typeof proxyFun==="function"){xhr[attribute]=function(){proxy[attribute](that)||v.apply(xhr,arguments)}}else{var attributeSetterHook=(proxyFun||{})["setter"];v=attributeSetterHook&&attributeSetterHook(v,that)||v;try{xhr[attribute]=v}catch(e){this[attribute+"_"]=v}}}}function hookFun(attribute){return function(){var args=[].slice.call(arguments);if(proxy[attribute]&&proxy[attribute].call(this,args,this.xhr)){return}try{return this.xhr[attribute].apply(this.xhr,args)}catch(e){}}}return window.NXMLHttpRequest};window.swapHeadAndPosition=function(arr,position){var first=arr[0];arr[0]=arr[position%arr.length];arr[position]=first;return arr};window.parsedecsig=function(body){var jsVarStr='[a-zA-Z_\\$][a-zA-Z_0-9]*';var jsSingleQuoteStr="'[^'\\\\]*(:?\\\\[\\s\\S][^'\\\\]*)*'";var jsDoubleQuoteStr="\"[^\"\\\\]*(:?\\\\[\\s\\S][^\"\\\\]*)*\"";var jsQuoteStr="(?:".concat(jsSingleQuoteStr,"|").concat(jsDoubleQuoteStr,")");var jsKeyStr="(?:".concat(jsVarStr,"|").concat(jsQuoteStr,")");var jsPropStr="(?:\\.".concat(jsVarStr,"|\\[").concat(jsQuoteStr,"\\])");var jsEmptyStr="(?:''|\"\")";var reverseStr=':function\\(a\\)\\{(?:return )?a\\.reverse\\(\\)\\}';var sliceStr=':function\\(a,b\\)\\{return a\\.slice\\(b\\)\\}';var spliceStr=':function\\(a,b\\)\\{a\\.splice\\(0,b\\)\\}';var swapStr=':function\\(a,b\\)\\{var c=a\\[0\\];a\\[0\\]=a\\[b(?:%a\\.length)?\\];a\\[b(?:%a\\.length)?\\]=c(?:;return a)?\\}';var actionsObjRegexp=new RegExp("var (".concat(jsVarStr,")=\\{((?:(?:")+jsKeyStr+reverseStr+'|'+jsKeyStr+sliceStr+'|'+jsKeyStr+spliceStr+'|'+jsKeyStr+swapStr+'),?\\r?\\n?)+)\\};');var actionsFuncRegexp=new RegExp("function(?: ".concat(jsVarStr,")?\\(a\\)\\{")+"a=a\\.split\\(".concat(jsEmptyStr,"\\);\\s*")+"((?:(?:a=)?".concat(jsVarStr)+jsPropStr+'\\(a,\\d+\\);)+)'+"return a\\.join\\(".concat(jsEmptyStr,"\\)")+'\\}');var reverseRegexp=new RegExp("(?:^|,)(".concat(jsKeyStr,")").concat(reverseStr),'m');var sliceRegexp=new RegExp("(?:^|,)(".concat(jsKeyStr,")").concat(sliceStr),'m');var spliceRegexp=new RegExp("(?:^|,)(".concat(jsKeyStr,")").concat(spliceStr),'m');var swapRegexp=new RegExp("(?:^|,)(".concat(jsKeyStr,")").concat(swapStr),'m');var objResult=actionsObjRegexp.exec(body);var funcResult=actionsFuncRegexp.exec(body);if(!objResult||!funcResult){return null}var obj=objResult[1].replace(/\$/g,'\\$');var objBody=objResult[2].replace(/\$/g,'\\$');var funcBody=funcResult[1].replace(/\$/g,'\\$');var result=reverseRegexp.exec(objBody);var reverseKey=result&&result[1].replace(/\$/g,'\\$').replace(/\$|^'|^"|'$|"$/g,'');result=sliceRegexp.exec(objBody);var sliceKey=result&&result[1].replace(/\$/g,'\\$').replace(/\$|^'|^"|'$|"$/g,'');result=spliceRegexp.exec(objBody);var spliceKey=result&&result[1].replace(/\$/g,'\\$').replace(/\$|^'|^"|'$|"$/g,'');result=swapRegexp.exec(objBody);var swapKey=result&&result[1].replace(/\$/g,'\\$').replace(/\$|^'|^"|'$|"$/g,'');var keys="(".concat([reverseKey,sliceKey,spliceKey,swapKey].join('|'),")");var myreg='(?:a=)?'+obj+"(?:\\.".concat(keys,"|\\['").concat(keys,"'\\]|\\[\"").concat(keys,"\"\\])")+'\\(a,(\\d+)\\)';var tokenizeRegexp=new RegExp(myreg,'g');var tokens=[];while((result=tokenizeRegexp.exec(funcBody))!==null){var key=result[1]||result[2]||result[3];switch(key){case swapKey:tokens.push('w'+result[4]);break;case reverseKey:tokens.push('r');break;case sliceKey:tokens.push('s'+result[4]);break;case spliceKey:tokens.push('p'+result[4]);break}}return tokens};window.youtubeDecipher=function(tokens,sig){sig=sig.split('');for(var i=0,len=tokens.length;i<len;i++){var token=tokens[i],pos;switch(token[0]){case'r':sig=sig.reverse();break;case'w':pos=~~token.slice(1);sig=window.swapHeadAndPosition(sig,pos);break;case's':pos=~~token.slice(1);sig=sig.slice(pos);break;case'p':pos=~~token.slice(1);sig.splice(0,pos);break}}return sig.join('')};window.analysis={'viu.tv':{'cycle':false,'code':function(){window.HookAjax({open:function(arg,xhr){if(arg[1]&&arg[1].includes('getVodURL')){xhr.onload=function(e){xhr=e.currentTarget;if(xhr.status===200){var response=xhr.responseText,url,formats;if(response){response=JSON.parse(response.responseText);formats=response.asset.hls.fix;if(url=window.checkAndFormatUrl(formats[formats.length-1][0])){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}}}}}})}},'facebook.com':{'cycle':false,'code':function(){window.addEventListener('play',function(e){if(e.target.tagName==='VIDEO'){if((parent=e.target.parentNode)&&parent.tagName==="DIV"){var data=parent.getAttribute("data-store"),dashManifest,formats=[],url;if(data){data=JSON.parse(data);if(url=window.checkAndFormatUrl(data.src)){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url,'quality':(data.width+'X'+data.height)}]))}}}}},true)}},'bilibili.com':{'cycle':false,'code':function(){var videos=document.getElementsByTagName('video'),url,videoInfo;if(videos&&videos.length&&window.__INITIAL_STATE__&&window.__INITIAL_STATE__.reduxAsyncConnect){if(videoInfo=window.__INITIAL_STATE__.reduxAsyncConnect.videoInfo){if(url=window.checkAndFormatUrl(videoInfo.initUrl)){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}}}},'instagram.com':{'cycle':false,'code':function(){document.addEventListener('click',function(e){if(e.target.tagName==='A'&&e.target.getAttribute('role')==='button'){var div=window.parentNodeFilter(e.target,'div','role','dialog'),url;if(url=window.checkAndFormatUrl(((div.getElementsByTagName('video'))[0]).src)){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}})}},'altbalaji.com':{'cycle':false,'code':function(){window.HookAjax({onload:function(xhr){if(xhr.responseURL.includes('media/series')&&xhr.responseURL.includes('episodes')&&xhr.responseURL.includes('&offset=')){var formats=[],count,response,edpis,streams,url;if(xhr.status===200&&(response=xhr.responseText)){response=JSON.parse(response);if((edpis=response.episodes)&&edpis.length){for(var i=0;i<edpis.length;i++){if(edpis[i].streams&&(streams=edpis[i].streams.web)&&streams.length){for(var j=0;j<streams.length;j++){if(streams[j]['type']==='m3u8'&&(url=window.checkAndFormatUrl(streams[j].src))){formats.push({'url':url,'title':edpis[i].titles.default?edpis[i].titles.default:''});break}}}count++}if(count===response.count){if(formats.length){window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}}}})}},'erosnow.com':{'cycle':false,'code':function(){window.HookAjax({onload:function(xhr){if(new RegExp('profiles\/\d*\?platform=').exec(xhr.responseURL)){var response,profiles,streams,url;if(xhr.status===200&&(response=xhr.responseText)){response=JSON.parse(response);if(profiles=response.profiles){for(var key in profiles){if((streams=profiles[key])&&streams){for(var i=0;i<streams.length;i++){if(streams[i].url&&streams[i].url.includes('.m3u8?')){if(url=window.checkAndFormatUrl(streams[i].url)){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}break}}}}}}}}})}},'dailymotion.com':{'cycle':false,'code':function(){window.HookAjax({open:function(arg,xhr){if(arg[1]&&arg[1].includes('player/metadata/video')){xhr.onload=function(e){xhr=e.currentTarget;if(xhr.status===200&&xhr.responseText){var response=JSON.parse(xhr.responseText),qualities,url,formats=[];if(qualities=response.qualities){for(var key in qualities){if(key!='auto'){for(var index=0;index<qualities[key].length;index++){url=window.checkAndFormatUrl(qualities[key][index].url);if(qualities[key][index].type==='video/mp4'&&url){formats.push({'url':url,'quality':key})}}}}if(formats.length){window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}}}})}},'xvideos':{'cycle':true,'code':function(){var html=document.getElementsByTagName('body')[0].innerHTML,src;if(html.includes('setVideoUrlHigh(')){src=window.middleSubString(html,"setVideoUrlHigh('","');");if(src=window.checkAndFormatUrl(src)){window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify([{'url':src}]))}}}},'porn.com':{'cycle':true,'code':function(){var content=document.getElementsByTagName('html')[0].innerHTML,streams,url,formats=[];if(content){streams=window.middleSubString(content,'{streams:',',hideMode:');if(streams=eval('('+streams+')')){for(var i=0;i<streams.length;i++){if(url=window.checkAndFormatUrl(streams[i].url)){formats.push({'url':url,'quality':streams.id})}}window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}},'pornhub.com':{'cycle':true,'code':function(){var content=document.getElementsByTagName('body')[0].innerHTML,streams,formats=[],url;if(content){streams=window.middleSubString(content,'mediaDefinitions":','}],');if(streams){if(streams=window.eval('('+streams+'}])')){for(var i=0;i<streams.length;i++){if(url=window.checkAndFormatUrl(streams[i].videoUrl)){formats.push({'url':url,'quality':streams[i].quality})}}window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}},'xnxx':{'cycle':true,'code':function(){if(html5player&&html5player.url_high){var url=window.checkAndFormatUrl(html5player.url_high);if(url){window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}}},'openloadmovies.bz':{'cycle':false,'code':function(){window.HookAjax({open:function(arg,xhr){if(arg[1]&&arg[1].includes('apiplayer/load.php')){xhr.onload=function(e){xhr=e.currentTarget;if(xhr.status===200&&xhr.responseText){try{var response=JSON.parse(xhr.responseText);if(response){response=window.middleSubString(response,'{"file":"','"');response=response.replace(new RegExp('\\\\','g'),'');response=window.checkAndFormatUrl(response);if(response){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':response}]))}}}catch(e){}}}}}})}},'timesnownews.com':{'cycle':true,'code':function(){var content=document.getElementById('schema_data').innerHTML,url;if(content){url=window.middleSubString(content,'"contentUrl": "','"');if(url=window.checkAndFormatUrl(url)){window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}}},'xhamster':{'cycle':true,'code':function(){var videoModel,sources,mp4,url,formats=[];if(window.initials){if(videoModel=window.initials.videoModel){if(sources=videoModel.sources){if(mp4=sources.mp4){for(var definition in mp4){if(url=window.checkAndFormatUrl(mp4[definition])){formats.push({'url':url,'quality':definition})}}window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}}},'tubidy.mobi':{'cycle':true,'code':function(){if(window.location.href.includes('watch.php')){var items=document.getElementsByClassName('list-group-item'),element,url,formats=[];if(items&&items.length){for(var index=0;index<items.length;index++){element=items[index].getElementsByTagName('a');if(element&&element.length){if(url=window.checkAndFormatUrl(element[0].getAttribute('href'))){formats.push({'url':url})}}}if(formats.length){window.VIDEO_CAPTURED_FLAG=true;window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}},'vimeo.com':{'cycle':false,'code':function(){window.addEventListener('play',function(e){if(e.target.tagName==='VIDEO'){var videoConfigURL=window.vimeo.config.config_url;if(videoConfigURL){var httpRequest=new XMLHttpRequest();httpRequest.open('GET',videoConfigURL);httpRequest.onreadystatechange=function(){if(httpRequest.readyState===XMLHttpRequest.DONE){if(httpRequest.status===200){var vimeoJSON=JSON.parse(httpRequest.responseText),formats=[];if(vimeoJSON&&vimeoJSON.request&&vimeoJSON.request.files&&vimeoJSON.request.files.progressive){var progressive=vimeoJSON.request.files.progressive;for(var j=0;j<progressive.length;j++){formats.push({'url':progressive[j].url,'quality':progressive[j].quality})}window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}};httpRequest.send()}}},true)}},'steampowered.com':{'cycle':false,'code':function(){window.addEventListener('play',function(e){if(e.target.tagName==='VIDEO'){var div=e.target.parentNode,formats=[];if(div&&div.tagName==='DIV'){if(url=window.checkAndFormatUrl(div.getAttribute('data-mp4-source'))){formats.push({'url':url,'quality':'480P'})}if(url=window.checkAndFormatUrl(div.getAttribute('data-mp4-hd-source'))){formats.push({'url':url,'quality':'720P'})}window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}},true)}},'soundcloud.com':{'cycle':false,'code':function(){window.HookAjax({open:function(arg,xhr){if(arg[1]&&arg[1].includes('/stream/progressive')){xhr.onload=function(e){xhr=e.currentTarget;if(xhr.status===200&&xhr.responseText){try{var response=JSON.parse(xhr.responseText);if(response){response=window.checkAndFormatUrl(response.url);if(response){window.androidVideoBridge.checkPlay(JSON.stringify([{'url':response}]))}}}catch(e){}}}}}})}}};(function(){window.console.log('CORE JAVASCRIPT INJECTED SUCCESSFUL!');window.INJECTION_FLAG=true,window.AJAX_HOOK_FLAG=false,window.VIDEO_CAPTURED_ARRAY=[],window.OPEN_NEW_WINDOW_LOCK=false;window.addEventListener("message",function(event){if(event.data){try{if(event.data.actionTag==='IFRAME_VIDEO_CAPTURED'){window.androidVideoBridge.checkPlay(JSON.stringify(event.data.videos))}if(event.data.actionTag==='IFRAME_YOUTUBE_CAPTURED'){var response=window.queryString2JSON(event.data.response);if(window.AUXILIARY_PARSE_JAVASCRIPT&&response&&response.url_encoded_fmt_stream_map){var decsig=window.parsedecsig(window.AUXILIARY_PARSE_JAVASCRIPT),stream_map=response.url_encoded_fmt_stream_map.split(','),stream,url,formats=[];if(decsig){for(var index in stream_map){if(stream=window.queryString2JSON(stream_map[index])){if(stream['type'].startsWith('video/mp4')&&(url=window.checkAndFormatUrl(stream.url))){formats.push({'url':url+(stream.s?'&sig='+window.youtubeDecipher(decsig,stream.s):''),'quality':stream.quality})}}}if(formats.length){window.androidVideoBridge.checkPlay(JSON.stringify(formats))}}}}}catch(error){}}},false);for(var domin in window.analysis){if(document.domain.includes(domin)){window.VIDEO_CAPTURED_MATCH=true;var obj=window.analysis[domin];if(obj.cycle){window.VIDEO_CAPTURED_INTERVAL=window.setInterval(function(){obj.code();if(window.VIDEO_CAPTURED_FLAG){window.clearInterval(window.VIDEO_CAPTURED_INTERVAL)}},1000)}else{obj.code()}break}}if(!window.VIDEO_CAPTURED_MATCH){window.GENERAL_CANPLAY_LISTENER=function(e){var video=e.target,url,quality;if((url=window.checkAndFormatUrl(video.currentSrc))&&!window.VIDEO_CAPTURED_ARRAY.includes(url)){window.VIDEO_CAPTURED_ARRAY.push(url);if(video.videoWidth){quality=video.videoWidth+'X'+video.videoHeight;window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url,'quality':quality}]))}else{window.androidVideoBridge.checkPlay(JSON.stringify([{'url':url}]))}}};window.addEventListener('canplay',window.GENERAL_CANPLAY_LISTENER,true)}})()}})();