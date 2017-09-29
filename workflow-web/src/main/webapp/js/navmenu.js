/**
 * 顶部导航菜单
 * @description 一级导航菜单
 * @author liuwanbin@git.com.cn
 * @version 1.0
 * @Example:
 */
(function ($) {
	$.fn.navmenu = function (options) {
		$(this).on('click', 'li', function(event) {
			$(this).parent().find('li.current').removeClass('current');
			$(this).addClass('current');
			var submenu=$(this).find("ul");
			if(submenu.length>0){
				if(submenu.is(":visible")){
					$(this).children("a").removeClass("s-icon");
					$(this).find("ul").slideUp(300);
				}else{
					$(this).children("a").addClass("s-icon");
					$(this).find("ul").slideDown(300);
				}
			}else{
				$(this).find("ul").slideUp(300);
			}			
			if(options && options.onClick && submenu.length == 0) {
				var id = $(this).attr('resId');
				var code = $(this).attr('id');
				var link = $(this).find('a');
				var name = link.text();
				var url = link.attr('href');
				options.onClick(event, id, code, name, url);
				event.stopPropagation();
			}
		});
		
		return this;
	};
})(jQuery);