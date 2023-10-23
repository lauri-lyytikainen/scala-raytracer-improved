package RayMath

import scala.math.*
/*
* The Vector3D class is a data type that contains all necessary vector operations for vector calculations in this program
*/
case class Vector3D(x: Double, y: Double, z: Double) {

  //add two vectors together
  def +(other: Vector3D) = Vector3D(this.x + other.x, this.y + other.y, this.z + other.z)

  //get the difference of two vectors
  def -(other: Vector3D) = Vector3D(this.x - other.x, this.y - other.y, this.z - other.z)

  //multiply this vector by a double or a set of doubles
  def *(double: Double) = Vector3D(this.x * double, this.y * double, this.z * double)

  def *(other: Vector3D) = Vector3D(this.x * other.x, this.y * other.y, this.z * other.z)

  //divide this vector by a double
  def /(double: Double) = Vector3D(this.x / double, this.y / double, this.z / double)

  //new vector with values forced with x, y or z
  def force(x: Option[Double], y: Option[Double], z: Option[Double]) = Vector3D(x.getOrElse(this.x), y.getOrElse(this.y), z.getOrElse(this.z))

  //get the length of this vector
  lazy val length = Math.sqrt(this dot this) //potential bug faulty length calculation

  //get a vector in the direction of this vector that has a length of 1
  def normalize = if (length != 0) Vector3D(this.x / length, this.y / length, this.z / length) else Vector3D(0, 0, 0)


  // Distamce between two vectors
  def distance(other: Vector3D) = (this - other).length

  // Return a copy of this vector
  def copy = Vector3D(this.x, this.y, this.z)
  //reversing functions
  def reverseX = new Vector3D(-this.x, this.y, this.z)
  def reverseY = new Vector3D(this.x, -this.y, this.z)
  def reverseZ = new Vector3D(this.x, this.y, -this.z)

  //dot product operation of this and other
  def dot(other: Vector3D): Double = this.x * other.x + this.y * other.y + this.z * other.z

  //get a vector that is the parallel component of this in the direction of other
  def parallelComponent(other: Vector3D): Vector3D = other * ((this dot other) / (other dot other))

  //get a vector that is the normal component of this in perpendicular to the direction of other
  def normalComponent(other: Vector3D): Vector3D = this - this.parallelComponent(other)

  //mirror this vector along other
  def mirror(other: Vector3D): Vector3D = this - this.normalComponent(other) * 2

  def reflect(normal: Vector3D): Vector3D = this - normal * 2 * (this dot normal)

  def clamp(min: Double, max: Double): Vector3D =
    Vector3D(
      x = if (this.x < min) min else if (this.x > max) max else this.x,
      y = if (this.y < min) min else if (this.y > max) max else this.y,
      z = if (this.z < min) min else if (this.z > max) max else this.z
    )
  def lerp(other: Vector3D, t: Double): Vector3D =
    Vector3D(
      x = this.x + (other.x - this.x) * t,
      y = this.y + (other.y - this.y) * t,
      z = this.z + (other.z - this.z) * t
    )

  def cross(other: Vector3D): Vector3D =
  Vector3D(
      x = this.y * other.z - this.z * other.y,
      y = this.z * other.x - this.x * other.z,
      z = this.x * other.y - this.y * other.x
    )


  override def toString =
    "x: "+this.x + ", y: "+this.y + ", z: "+this.z
}

class RotationMatrix3D(xtilt: Double, ytilt: Double, ztilt: Double){

  def this(array: Array[Array[Double]]) =
    this(0,0,0)
    this.mat = array

  val (sinx, cosx) = (sin(xtilt), cos(xtilt))
  val (siny, cosy) = (sin(ytilt), cos(ytilt))
  val (sinz, cosz) = (sin(ztilt), cos(ztilt))
  //println("cos,sin values are for x,y,z tilts: ")
  //println(sinx + " "+ cosx + " " + siny + " " + cosy + " " + sinz + " " + cosz)
  //Rotational matrix based on x,y and z tilts (roll, pitch and yaw)
  var mat: Array[Array[Double]] = Array(
    Array(cosy*cosz, sinx*siny*cosz-cosx*sinz, cosx*siny*cosz+sinx*sinz),
    Array(cosy*sinz, sinx*siny*sinz+cosx*cosz, cosx*siny*sinz-sinx*cosz),
    Array(  -siny,          sinx*cosy,                  cosx*cosy      )
  )

  //debug for matrix
  //mat.foreach(_.zipWithIndex.foreach((a,i) => println(i+1 + ": " + a)))

  //Produces a vector that has rotations applied to it.
  def *(vec: Vector3D): Vector3D =
    val m1 = mat(0)
    val m2 = mat(1)
    val m3 = mat(2)
    Vector3D(
    x = vec.x*m1(0)+vec.y*m1(1)+vec.z*m1(2),
    y = vec.x*m2(0)+vec.y*m2(1)+vec.z*m2(2),
    z = vec.x*m3(0)+vec.y*m3(1)+vec.z*m3(2)
    )

  def rotatePoint(point: Vector3D, origin: Vector3D): Vector3D = {
    val rotated = this * (point - origin)
    rotated + origin
  }

  def rotateRay(ray: Ray, origin: Vector3D): Ray = {
    val newOrigin = this * (ray.origin - origin) + origin
    val newDirection = (this * ray.direction).normalize
    Ray(newOrigin, newDirection)
  }

  // Inverse of this matrix
  def invert: RotationMatrix3D =
    val m1 = mat(0)
    val m2 = mat(1)
    val m3 = mat(2)
    new RotationMatrix3D(Array(
    Array(m1(0), m2(0), m3(0)),
    Array(m1(1), m2(1), m3(1)),
    Array(m1(2), m2(2), m3(2))
    ))

}

