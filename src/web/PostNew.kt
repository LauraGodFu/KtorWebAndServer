//package web
//
//import com.geely.gic.hmi.web.Login
//import com.geely.gic.hmi.web.PostNew
//import com.geely.gic.hmi.web.ViewKweet
//import com.geely.gic.hmi.data.dao.DAOFacade
//import com.geely.gic.hmi.data.model.Session
//import com.geely.gic.hmi.security.securityCode
//import com.geely.gic.hmi.security.verifyCode
//import com.geely.gic.hmi.web.redirect
//import io.ktor.application.*
//import io.ktor.freemarker.*
//import io.ktor.http.*
//import io.ktor.request.*
//import io.ktor.response.*
//import io.ktor.routing.*
//import io.ktor.sessions.*
//import io.ktor.locations.*
//
///**
// * Register routes for the [PostNew] route '/post-new'
// */
//fun Route.postNew(dao: DAOFacade, hashFunction: (String) -> String) {
//    /**
//     * A GET request returns a page with a form to post a new Kweet in the case the user
//     * is logged also generating a [code] token to prevent.
//     *
//     * If the user is not logged it redirects to the [Login] page.
//     */
//    get<PostNew> {
//        val user = call.sessions.get<Session>()?.let { dao.user(it.userId) }
//
//        if (user == null) {
//            call.redirect(Login())
//        } else {
//            val date = System.currentTimeMillis()
//            val code = call.securityCode(date, user, hashFunction)
//
//            call.respond(FreeMarkerContent("new-kweet.ftl", mapOf("user" to user, "date" to date, "code" to code), user.userId))
//        }
//    }
//    /**
//     * A POST request actually tries to create a new [Kweet].
//     * It validates the `date`, `code` and `text` parameters and redirects to the login page on failure.
//     * On success it creates the new [Kweet] and redirect to the [ViewKweet] page to view that specific Kweet.
//     */
//    post<PostNew> {
//        val user = call.sessions.get<Session>()?.let { dao.user(it.userId) }
//
//        val post = call.receive<Parameters>()
//        val date = post["date"]?.toLongOrNull() ?: return@post call.redirect(it)
//        val code = post["code"] ?: return@post call.redirect(it)
//        val text = post["text"] ?: return@post call.redirect(it)
//
//        if (user == null || !call.verifyCode(date, user, code, hashFunction)) {
//            call.redirect(Login())
//        } else {
//            val id = dao.createKweet(user.userId, text, null)
//            call.redirect(ViewKweet(id))
//        }
//    }
//}