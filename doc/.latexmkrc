add_cus_dep( 'acn', 'acr', 0, 'makeglossaries' );
add_cus_dep( 'glo', 'gls', 0, 'makeglossaries' );
$clean_ext .= " acr acn alg glo gls glg";
sub makeglossaries {
  my ($base_name, $path) = fileparse( $_[0] );
  pushd $path;
  my $return = system "makeglossaries", $base_name;
  popd;
  return $return;
}

$out_dir = "./out";
$aux_dir = "./int";
$emulate_aux = 1;
push @generated_exts, 'glo', 'gls', 'glg';
push @generated_exts, 'acn', 'acr', 'alg';
$clean_ext = "lol run.xml bbl %R.ist %R.xdy";
