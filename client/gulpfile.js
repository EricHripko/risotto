var gulp = require("gulp");
var sass = require("gulp-sass");
var browserSync = require('browser-sync').create(); 
var reload = browserSync.reload; 

gulp.task("styles", function(){
    gulp.src('sass/*.sass')
    .pipe(sass())
    .pipe(gulp.dest('app/client/css'))
    .pipe(browserSync.reload({stream: true})); 
}); 

gulp.task('serve', function(){
       browserSync.init({
       server: {
        baseDire: './'
       }
       }); 

gulp.watch('sass/*.sass', ['styles']); 
gulp.watch('*.html').on('change', browserSync.reload);  
gulp.watch('css/*.css').on('change', browserSync.reload); 
gulp.watch('scripts/*.js').on('change', browserSync.reload); 
    
}); 

gulp.task('default', ['styles', 'serve']); 