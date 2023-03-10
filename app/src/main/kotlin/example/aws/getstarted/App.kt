/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package example.aws.getstarted

import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.*
import aws.smithy.kotlin.runtime.content.ByteStream
import kotlinx.coroutines.runBlocking
import java.util.*

const val REGION = "us-west-2"
const val BUCKET = "bucket-sanskar"
val KEY = "key-${UUID.randomUUID()}"

class App {
    val greeting: String
        get() {
            return "Hello World!"
        }
}

fun main(): Unit = runBlocking {
    S3Client.fromEnvironment { region = REGION }
        .use { s3 ->
            setupTutorial(s3)
            println("Creating object $BUCKET/$KEY...")

            s3.putObject(PutObjectRequest {
                bucket = BUCKET
                key = KEY
                body = ByteStream.fromString("Testing with the Kotlin SDK")
            })

            println("Object $BUCKET/$KEY created successfully!")

            cleanUp(s3)
        }
}

suspend fun setupTutorial(s3: S3Client) {
    println("Creating bucket $BUCKET...")

    try {
        s3.createBucket(CreateBucketRequest {
            bucket = BUCKET
            createBucketConfiguration {
                locationConstraint = BucketLocationConstraint.fromValue(REGION)
            }
        })
        println("Bucket $BUCKET created successfully!")
    } catch(e: BucketAlreadyOwnedByYou) {
        println(e)
    } catch (e: BucketAlreadyExists) {
        println(e)
    }
}

suspend fun cleanUp(s3: S3Client) {
    println("Deleting object $BUCKET/$KEY...")
    s3.deleteObject(DeleteObjectRequest {
        bucket = BUCKET
        key = KEY
    })

    println("Object $BUCKET/$KEY deleted successfully!")

    println("Deleting bucket $BUCKET...")
    s3.deleteBucket(DeleteBucketRequest {
        bucket = BUCKET
    })
    println("Bucket $BUCKET deleted successfully!")
}
