var gulp = require("gulp");
var sass = require("gulp-sass");

gulp.task("styles", function() {
    return gulp.src("sass/*.sass")
        .pipe(sass().on('error', sass.logError))
        .pipe(gulp.dest("css"));
});