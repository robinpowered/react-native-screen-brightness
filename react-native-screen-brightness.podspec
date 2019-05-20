require 'json'
package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = "react-native-screen-brightness"
  s.version      = package['version']
  s.summary      = "RNIap"
  s.description  = package['description']
  s.homepage     = "https://github.com/robinpowered/react-native-screen-brightness"
  s.license      = package['license']
  s.author       = package['author']
  s.platform     = :ios, "8.0"
  s.source                 = { :git => 'https://github.com/robinpowered/react-native-screen-brightness.git', :tag => s.version }
  s.source_files  = "ios/**/*.{h,m}"
  s.requires_arc = true

  s.dependency 'React'
end
