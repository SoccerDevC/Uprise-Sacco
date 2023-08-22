<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Failed_Login extends Model
{
    use HasFactory;

    protected $table = 'failed_login';

    
    protected $fillable = ['message'];

    protected $primaryKey = 'reference_number';
    public $incrementing = false;


}
