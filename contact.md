---
title: Contact the Arden2ByteCode Team
nav_title: Contact
custom_js:
 - https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js
---

![TU Braunschweig Logo](https://www.tu-braunschweig.de/icons/tubsdesign/siegel_rot.jpg)

Arden2ByteCode was created at the [Technische Universität Braunschweig](http://www.tu-braunschweig.de) in cooperation between the [Peter L. Reichertz Institute for Medical Informatics](http://www.plri.de/Arden2ByteCode.html) and the [Institute for Programming and Reactive Systems](http://www.ips.cs.tu-bs.de/).

Please refer to the following list (in alphabetical order).  
You may contact any of the projects contributors via their respective profile pages. :

*   [Hannes Flicka](http://github.com/hflicka) (E-Mail: <a class="mail" href="vmyzi2wtozxjyz@bhvdg.xjh"></a>)
*   [Matthias Gietzelt](http://www.plri.de/index.php?id=gietzelt)
*   [Prof. Dr. Ursula Goltz](https://www.tu-braunschweig.de/ips/staff/goltz)
*   [Daniel Grunwald](https://github.com/dgrunwald)
*   [Prof. Dr. Reinhold Haux](http://www.plri.de/mitarbeiter/haux.html)
*   [Mike Klimek](https://github.com/Tetr4)
*   [Malte Lochau](https://www.tu-braunschweig.de/ips/staff/lochau)
*   [Prof. Dr. Dr. Michael Marschollek](http://www.plri.de/index.php?id=marschollek)
*   [Bianying Song](http://www.plri.de/index.php?id=song)
*   [Dr. Klaus-Hendrik Wolf](http://www.plri.de/mitarbeiter/wolf.html)

<script type="text/javascript">
$(function() {
	$('a.mail').each(function(index, element) {
		var addr = $(element).attr('href');
		addr = addr.replace(/[a-zA-Z]/g, function(c) {
			return String.fromCharCode((c <= "Z" ? 90 : 122) >= (c = c.charCodeAt(0) + 5)? c : c - 26);
		});
		$(element).attr('href', 'mailto:' + addr);
		$(element).text(addr);
	});
});
</script>
