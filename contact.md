---
title: Contact the Arden2ByteCode Team
nav_title: Contact
nav_order: 7
custom_js:
 - https://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js
---

![TU Braunschweig Logo](https://www.tu-braunschweig.de/icons/tubsdesign/siegel_rot.jpg)

Arden2ByteCode was created at the [Technische Universität Braunschweig](http://www.tu-braunschweig.de) in cooperation between the [Peter L. Reichertz Institute for Medical Informatics](https://plri.de/) and the [Institute for Programming and Reactive Systems](http://www.ips.cs.tu-bs.de/).

Project manager and contact person:

*   [Dr. Klaus-Hendrik Wolf](https://plri.de/en/mitarbeiter/klaus-hendrik_wolf)

Contributors (in alphabetical order):

*   [Hannes Flicka](https://github.com/hflicka)
*   Matthias Gietzelt
*   [Prof. Dr. Ursula Goltz](https://www.tu-braunschweig.de/ips/staff/goltz)
*   [Daniel Grunwald](https://github.com/dgrunwald)
*   [Prof. Dr. Reinhold Haux](https://plri.de/en/mitarbeiter/reinhold-haux)
*   [Mike Klimek](https://github.com/Tetr4)
*   [Malte Lochau](https://www.tu-braunschweig.de/ips/staff/former/lochau)
*   [Prof. Dr. Dr. Michael Marschollek](https://plri.de/en/mitarbeiter/michael_marschollek)
*   Bianying Song

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
