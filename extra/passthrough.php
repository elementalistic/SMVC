<?php
$options = array(
	'http' => array(
	'header'  => "Content-type: application/json\r\n",
	'method'  => $_SERVER['REQUEST_METHOD'],
	),
);
	$context = stream_context_create($options);
	$contents = file_get_contents($url, false, $context);
	print $contents;